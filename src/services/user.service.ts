import { IUser, User } from '../models/user.model';
import { StampDefinition, UserStamp } from '../models/stamp.model';
import { Activity } from '../models/activity.model';
import { Favorite } from '../models/favorite.model';
import { Business } from '../models/business.model';

export type UserProfileResponse = {
  id: string;
  name: string;
  email: string;
  phone: string;
  whatsapp: string;
  profilePhotoUrl: string;
  role: string;
  coins: number;
  level: number;
  levelName: string;
  createdAt: Date;
};

function toProfile(user: IUser): UserProfileResponse {
  return {
    id: (user._id as any).toString(),
    name: user.name || '',
    email: user.email || '',
    phone: user.phone || '',
    whatsapp: user.whatsapp || user.phone || '',
    profilePhotoUrl: user.profilePhotoUrl || '',
    role: user.role,
    coins: user.coins ?? 0,
    level: user.level ?? 1,
    levelName: user.levelName || 'Nuevo',
    createdAt: user.createdAt,
  };
}

export async function getMyProfile(userId: string): Promise<UserProfileResponse> {
  const user = await User.findById(userId);
  if (!user) throw Object.assign(new Error('Usuario no encontrado'), { status: 404 });
  return toProfile(user);
}

export async function updateMyProfile(
  userId: string,
  updates: Partial<Pick<IUser, 'name' | 'phone' | 'whatsapp' | 'profilePhotoUrl'>>
): Promise<UserProfileResponse> {
  const patch: Record<string, string> = {};

  if (typeof updates.name === 'string') patch.name = updates.name.trim();
  if (typeof updates.phone === 'string') patch.phone = updates.phone.trim();
  if (typeof updates.whatsapp === 'string') patch.whatsapp = updates.whatsapp.trim();
  if (typeof updates.profilePhotoUrl === 'string') patch.profilePhotoUrl = updates.profilePhotoUrl.trim();

  const user = await User.findByIdAndUpdate(
    userId,
    { $set: patch },
    { new: true, runValidators: true }
  );

  if (!user) throw Object.assign(new Error('Usuario no encontrado'), { status: 404 });
  return toProfile(user);
}

export async function getMyStats(userId: string) {
  const [favoriteCount, stampCount, activityItems, reviewCount] = await Promise.all([
    Favorite.countDocuments({ userId }),
    UserStamp.countDocuments({ userId }),
    Activity.countDocuments({ userId, type: 'visit' }),
    Activity.countDocuments({ userId, type: 'review' }),
  ]);

  return [
    { v: String(activityItems), l: 'Visitados', emoji: '🚶' },
    { v: String(favoriteCount), l: 'Favoritos', emoji: '♥' },
    { v: String(reviewCount), l: 'Reseñas', emoji: '✍️' },
    { v: String(stampCount), l: 'Sellos', emoji: '🏆' },
  ];
}

export async function getMyStamps(userId: string) {
  const [allStamps, userStamps] = await Promise.all([
    StampDefinition.find().sort({ order: 1 }).lean(),
    UserStamp.find({ userId }).lean(),
  ]);

  const earnedIds = new Set(userStamps.map(us => (us.stampId as any).toString()));

  return allStamps.map(s => ({
    id: (s._id as any).toString(),
    label: s.label,
    emoji: s.emoji,
    color: s.color,
    got: earnedIds.has((s._id as any).toString()),
  }));
}

export async function getMyActivity(userId: string, limit = 20) {
  const items = await Activity.find({ userId })
    .sort({ createdAt: -1 })
    .limit(limit)
    .lean();

  return items.map(a => ({
    id: (a._id as any).toString(),
    emoji: a.emoji,
    text: a.text,
    when: formatRelativeTime(a.createdAt),
    coins: a.coins >= 0 ? `+${a.coins}` : String(a.coins),
  }));
}

export async function getMyCollection(userId: string) {
  const favorites = await Favorite.find({ userId })
    .sort({ createdAt: -1 })
    .lean();

  if (favorites.length === 0) return [];

  const bizIds = favorites.map(f => f.businessId);
  const businesses = await Business.find({ _id: { $in: bizIds } })
    .select('name emoji color')
    .lean();

  const bizMap = new Map(businesses.map(b => [(b._id as any).toString(), b]));

  return favorites
    .map(f => {
      const biz = bizMap.get((f.businessId as any).toString());
      if (!biz) return null;
      return {
        id: (f._id as any).toString(),
        e: biz.emoji || '🏪',
        c: biz.color || '#FFB57A',
        n: biz.name,
      };
    })
    .filter(Boolean);
}

function formatRelativeTime(date: Date): string {
  const now = Date.now();
  const diff = now - date.getTime();
  const minutes = Math.floor(diff / 60_000);
  const hours = Math.floor(diff / 3_600_000);
  const days = Math.floor(diff / 86_400_000);
  const weeks = Math.floor(diff / 604_800_000);

  if (minutes < 1) return 'Ahora';
  if (minutes < 60) return `Hace ${minutes}m`;
  if (hours < 24) return `Hace ${hours}h`;
  if (days < 7) return `Hace ${days} día${days > 1 ? 's' : ''}`;
  if (weeks < 5) return `Hace ${weeks} sem`;
  return `Hace ${Math.floor(days / 30)} mes${Math.floor(days / 30) > 1 ? 'es' : ''}`;
}

import { Business, IBusiness, IMenuItem, IPromo } from '../models/business.model';
import { Analytics } from '../models/analytics.model';

export async function getMyBusiness(userId: string): Promise<IBusiness> {
  const biz = await Business.findOne({ ownerId: userId });
  if (!biz) throw Object.assign(new Error('Negocio no encontrado'), { status: 404 });
  return biz;
}

export async function updateMyBusiness(userId: string, updates: Partial<IBusiness>): Promise<IBusiness> {
  const biz = await Business.findOneAndUpdate(
    { ownerId: userId },
    { $set: updates },
    { new: true, runValidators: true }
  );
  if (!biz) throw Object.assign(new Error('Negocio no encontrado'), { status: 404 });
  return biz;
}

export async function replaceMenu(userId: string, items: IMenuItem[]): Promise<IBusiness> {
  const biz = await Business.findOneAndUpdate(
    { ownerId: userId },
    { $set: { menuItems: items } },
    { new: true }
  );
  if (!biz) throw Object.assign(new Error('Negocio no encontrado'), { status: 404 });

  await Analytics.findOneAndUpdate(
    { businessId: biz._id },
    { $set: { menuItemCount: items.length } }
  );

  return biz;
}

export async function setPromo(userId: string, promo: Omit<IPromo, 'active'>): Promise<IBusiness> {
  const biz = await Business.findOneAndUpdate(
    { ownerId: userId },
    { $set: { activePromo: { ...promo, active: true } } },
    { new: true }
  );
  if (!biz) throw Object.assign(new Error('Negocio no encontrado'), { status: 404 });

  await Analytics.findOneAndUpdate(
    { businessId: biz._id },
    { $set: { hasActivePromo: true } }
  );

  return biz;
}

export async function deletePromo(userId: string): Promise<IBusiness> {
  const biz = await Business.findOneAndUpdate(
    { ownerId: userId },
    { $set: { activePromo: null } },
    { new: true }
  );
  if (!biz) throw Object.assign(new Error('Negocio no encontrado'), { status: 404 });

  await Analytics.findOneAndUpdate(
    { businessId: biz._id },
    { $set: { hasActivePromo: false } }
  );

  return biz;
}

export async function getAnalytics(userId: string) {
  const biz = await Business.findOne({ ownerId: userId });
  if (!biz) throw Object.assign(new Error('Negocio no encontrado'), { status: 404 });

  const analytics = await Analytics.findOne({ businessId: biz._id });
  if (!analytics) {
    return {
      businessId: (biz._id as any).toString(),
      businessName: biz.name,
      visitCount: biz.visitCount,
      reviewCount: biz.reviewCount,
      rating: biz.rating,
      menuItemCount: biz.menuItems.length,
      hasActivePromo: !!biz.activePromo,
      isTrending: biz.trending,
      totalOrders: 0,
      topMenuItem: biz.menuItems.length > 0 ? biz.menuItems[0].name : null,
      recentReviews: [],
    };
  }

  return analytics;
}

export async function getAllBusinesses() {
  return Business.find()
    .select('name categoryId description address neighborhood rating reviewCount emoji color tags tag activePromo trending nuevo createdAt')
    .sort({ trending: -1, nuevo: -1, createdAt: -1 })
    .limit(80)
    .lean();
}

export async function getBusinessById(id: string) {
  const biz = await Business.findById(id);
  if (!biz) throw Object.assign(new Error('Negocio no encontrado'), { status: 404 });
  return biz;
}

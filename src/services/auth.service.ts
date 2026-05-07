import bcrypt from 'bcrypt';
import { User, IUser } from '../models/user.model';
import { Business } from '../models/business.model';
import { Analytics } from '../models/analytics.model';
import { signToken } from '../utils/jwt.util';

interface RegisterBusinessInput {
  name: string;
  email?: string;
  whatsapp?: string;
  password: string;
  businessName: string;
  phone?: string;
  address?: string;
  description?: string;
}

interface RegisterInput {
  name: string;
  email: string;
  password: string;
}

function buildAuthResponse(user: IUser, businessId: string | null) {
  return {
    token: signToken({ userId: (user._id as any).toString(), role: user.role }),
    userId: (user._id as any).toString(),
    name: user.name,
    email: user.email || '',
    role: user.role,
    businessId,
    coins: user.coins,
    level: user.level,
    levelName: user.levelName,
  };
}

export async function registerBusiness(input: RegisterBusinessInput) {
  const identifier = input.email || input.whatsapp;
  const existing = await User.findOne({
    $or: [
      ...(input.email ? [{ email: input.email }] : []),
      ...(input.whatsapp ? [{ whatsapp: input.whatsapp }] : []),
    ],
  });
  if (existing) throw Object.assign(new Error('Este email o WhatsApp ya está registrado'), { status: 409 });

  const passwordHash = await bcrypt.hash(input.password, 10);
  const user = await User.create({
    name: input.name,
    email: input.email || undefined,
    whatsapp: input.whatsapp || undefined,
    phone: input.phone || input.whatsapp || '',
    passwordHash,
    role: 'BUSINESS',
  });

  const business = await Business.create({
    ownerId: user._id,
    name: input.businessName,
    description: input.description || '',
    address: input.address || '',
    phone: input.phone || '',
  });

  await Analytics.create({
    businessId: business._id,
    businessName: business.name,
  });

  return buildAuthResponse(user, (business._id as any).toString());
}

export async function registerUser(input: RegisterInput) {
  const existing = await User.findOne({ email: input.email.toLowerCase() });
  if (existing) throw Object.assign(new Error('El email ya está registrado'), { status: 409 });

  const passwordHash = await bcrypt.hash(input.password, 12);
  const user = await User.create({
    name: input.name,
    email: input.email.toLowerCase().trim(),
    passwordHash,
    role: 'USER',
    businessId: null,
    coins: 0,
    level: 1,
    levelName: 'Nuevo',
  });

  return buildAuthResponse(user, null);
}

export async function login(identifier: string, password: string) {
  const user = await User.findOne({
    $or: [
      { email: identifier },
      { phone: identifier },
      { whatsapp: identifier },
    ],
  });
  if (!user) throw Object.assign(new Error('Credenciales inválidas'), { status: 401 });

  const valid = await bcrypt.compare(password, user.passwordHash);
  if (!valid) throw Object.assign(new Error('Credenciales inválidas'), { status: 401 });

  let businessId: string | null = null;
  if (user.role === 'BUSINESS') {
    const biz = await Business.findOne({ ownerId: user._id });
    businessId = biz ? (biz._id as any).toString() : null;
  }

  return buildAuthResponse(user, businessId);
}

export async function adminLogin(email: string, password: string) {
  const user = await User.findOne({ email, role: 'ADMIN' });
  if (!user) throw Object.assign(new Error('Credenciales inválidas'), { status: 401 });

  const valid = await bcrypt.compare(password, user.passwordHash);
  if (!valid) throw Object.assign(new Error('Credenciales inválidas'), { status: 401 });

  return buildAuthResponse(user, null);
}

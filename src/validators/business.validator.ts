import { z } from 'zod';

const dayScheduleSchema = z.object({
  isOpen: z.boolean(),
  openTime: z.string(),
  openPeriod: z.enum(['AM', 'PM']),
  closeTime: z.string(),
  closePeriod: z.enum(['AM', 'PM']),
});

const paymentMethodsSchema = z.object({
  efectivo: z.boolean(),
  tarjetas: z.boolean(),
  transferencia: z.boolean(),
});

export const updateBusinessSchema = z.object({
  name: z.string().min(1).max(100).optional(),
  description: z.string().max(600).optional(),
  address: z.string().max(300).optional(),
  neighborhood: z.string().max(100).optional(),
  phone: z.string().optional(),
  whatsapp: z.string().max(20).optional(),
  instagram: z.string().max(100).optional(),
  facebook: z.string().max(100).optional(),
  tiktok: z.string().max(100).optional(),
  youtube: z.string().max(100).optional(),
  categoryId: z.string().optional(),
  paymentMethod: z.string().optional(),
  paymentMethods: paymentMethodsSchema.optional(),
  emoji: z.string().optional(),
  color: z.string().regex(/^#[0-9A-Fa-f]{6}$/, 'Color hexadecimal inválido').optional(),
  tags: z.array(z.string()).optional(),
  tag: z.string().optional(),
  priceRange: z.string().optional(),
  schedule: z.string().optional(),
  weeklySchedule: z.record(z.string(), dayScheduleSchema).optional(),
  locationText: z.string().optional(),
  photos: z.array(z.string()).max(6).optional(),
}).strict();

export const menuItemSchema = z.object({
  name: z.string().min(1, 'Nombre requerido'),
  emoji: z.string().default('🍽️'),
  price: z.string().min(1, 'Precio requerido'),
  orderCount: z.number().default(0),
});

export const menuItemsArraySchema = z.array(menuItemSchema);

export const promoSchema = z.object({
  title: z.string().min(1, 'Título requerido'),
  label: z.string().default(''),
  validUntil: z.string().default(''),
  note: z.string().default(''),
  bonusCoins: z.number().default(0),
});

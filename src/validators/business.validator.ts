import { z } from 'zod';

export const updateBusinessSchema = z.object({
  name: z.string().min(1).optional(),
  description: z.string().optional(),
  address: z.string().optional(),
  neighborhood: z.string().optional(),
  phone: z.string().optional(),
  instagram: z.string().optional(),
  categoryId: z.string().optional(),
  paymentMethod: z.string().optional(),
  emoji: z.string().optional(),
  color: z.string().optional(),
  tags: z.array(z.string()).optional(),
  tag: z.string().optional(),
  priceRange: z.string().optional(),
  schedule: z.string().optional(),
  locationText: z.string().optional(),
  photos: z.array(z.string()).optional(),
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

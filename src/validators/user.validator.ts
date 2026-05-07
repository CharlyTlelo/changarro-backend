import { z } from 'zod';

const phoneLike = z
  .string()
  .trim()
  .min(7, 'Teléfono muy corto')
  .max(25, 'Teléfono muy largo')
  .regex(/^[0-9+\-\s()]+$/, 'Formato de teléfono inválido');

export const updateUserProfileSchema = z
  .object({
    name: z.string().trim().min(2, 'Nombre mínimo 2 caracteres').max(60, 'Nombre máximo 60 caracteres').optional(),
    whatsapp: phoneLike.optional(),
    phone: phoneLike.optional(),
    profilePhotoUrl: z.string().max(5_000_000, 'La imagen es demasiado grande').optional(),
  })
  .strict()
  .refine((d) => Object.keys(d).length > 0, {
    message: 'No hay campos para actualizar',
  });


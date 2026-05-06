import { z } from 'zod';

export const registerBusinessSchema = z.object({
  name: z.string().min(2, 'Nombre requerido'),
  email: z.string().email('Email inválido'),
  password: z.string().min(6, 'Contraseña mínimo 6 caracteres'),
  businessName: z.string().min(2, 'Nombre del negocio requerido'),
  phone: z.string().optional(),
  address: z.string().optional(),
  description: z.string().optional(),
});

export const registerSchema = z.object({
  name: z.string().min(2, 'Nombre mínimo 2 caracteres').max(60, 'Nombre máximo 60 caracteres'),
  email: z.string().email('Email inválido').transform(v => v.toLowerCase().trim()),
  password: z.string().min(8, 'Contraseña mínimo 8 caracteres').max(72, 'Contraseña máximo 72 caracteres'),
});

export const loginSchema = z.object({
  email: z.string().email('Email inválido'),
  password: z.string().min(1, 'Contraseña requerida'),
});

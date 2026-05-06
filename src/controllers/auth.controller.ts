import { Request, Response, NextFunction } from 'express';
import { registerBusinessSchema, registerSchema, loginSchema } from '../validators/auth.validator';
import * as authService from '../services/auth.service';

export async function registerBusiness(req: Request, res: Response, next: NextFunction) {
  try {
    const data = registerBusinessSchema.parse(req.body);
    const result = await authService.registerBusiness(data);
    res.status(201).json(result);
  } catch (err) { next(err); }
}

export async function register(req: Request, res: Response, next: NextFunction) {
  try {
    const data = registerSchema.parse(req.body);
    const result = await authService.registerUser(data);
    res.status(201).json(result);
  } catch (err) { next(err); }
}

export async function login(req: Request, res: Response, next: NextFunction) {
  try {
    const data = loginSchema.parse(req.body);
    const result = await authService.login(data.email, data.password);
    res.json(result);
  } catch (err) { next(err); }
}

export async function adminLogin(req: Request, res: Response, next: NextFunction) {
  try {
    const data = loginSchema.parse(req.body);
    const result = await authService.adminLogin(data.email, data.password);
    res.json(result);
  } catch (err) { next(err); }
}

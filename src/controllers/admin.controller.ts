import { Request, Response, NextFunction } from 'express';
import * as bizService from '../services/business.service';
import { User } from '../models/user.model';

export async function getAllBusinesses(req: Request, res: Response, next: NextFunction) {
  try {
    const businesses = await bizService.getAllBusinesses();
    res.json(businesses);
  } catch (err) { next(err); }
}

export async function getBusinessById(req: Request, res: Response, next: NextFunction) {
  try {
    const biz = await bizService.getBusinessById(req.params.id);
    res.json(biz);
  } catch (err) { next(err); }
}

export async function getAllUsers(req: Request, res: Response, next: NextFunction) {
  try {
    const users = await User.find().select('-passwordHash').lean();
    res.json(users);
  } catch (err) { next(err); }
}

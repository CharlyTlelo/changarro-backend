import { Request, Response, NextFunction } from 'express';
import { updateBusinessSchema, menuItemsArraySchema, promoSchema } from '../validators/business.validator';
import * as bizService from '../services/business.service';

export async function getMyBusiness(req: Request, res: Response, next: NextFunction) {
  try {
    const biz = await bizService.getMyBusiness(req.auth!.userId);
    res.json(biz);
  } catch (err) { next(err); }
}

export async function updateMyBusiness(req: Request, res: Response, next: NextFunction) {
  try {
    const updates = updateBusinessSchema.parse(req.body);
    const biz = await bizService.updateMyBusiness(req.auth!.userId, updates);
    res.json(biz);
  } catch (err) { next(err); }
}

export async function replaceMenu(req: Request, res: Response, next: NextFunction) {
  try {
    const items = menuItemsArraySchema.parse(req.body);
    const biz = await bizService.replaceMenu(req.auth!.userId, items);
    res.json(biz);
  } catch (err) { next(err); }
}

export async function setPromo(req: Request, res: Response, next: NextFunction) {
  try {
    const promo = promoSchema.parse(req.body);
    const biz = await bizService.setPromo(req.auth!.userId, promo);
    res.json(biz);
  } catch (err) { next(err); }
}

export async function deletePromo(req: Request, res: Response, next: NextFunction) {
  try {
    const biz = await bizService.deletePromo(req.auth!.userId);
    res.json(biz);
  } catch (err) { next(err); }
}

export async function getAnalytics(req: Request, res: Response, next: NextFunction) {
  try {
    const data = await bizService.getAnalytics(req.auth!.userId);
    res.json(data);
  } catch (err) { next(err); }
}

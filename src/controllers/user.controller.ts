import { NextFunction, Request, Response } from 'express';
import { updateUserProfileSchema } from '../validators/user.validator';
import * as userService from '../services/user.service';

export async function getMyProfile(req: Request, res: Response, next: NextFunction) {
  try {
    const profile = await userService.getMyProfile(req.auth!.userId);
    res.json(profile);
  } catch (err) {
    next(err);
  }
}

export async function updateMyProfile(req: Request, res: Response, next: NextFunction) {
  try {
    const updates = updateUserProfileSchema.parse(req.body);
    const profile = await userService.updateMyProfile(req.auth!.userId, updates);
    res.json(profile);
  } catch (err) {
    next(err);
  }
}

export async function getMyStats(req: Request, res: Response, next: NextFunction) {
  try {
    const stats = await userService.getMyStats(req.auth!.userId);
    res.json(stats);
  } catch (err) {
    next(err);
  }
}

export async function getMyStamps(req: Request, res: Response, next: NextFunction) {
  try {
    const stamps = await userService.getMyStamps(req.auth!.userId);
    res.json(stamps);
  } catch (err) {
    next(err);
  }
}

export async function getMyActivity(req: Request, res: Response, next: NextFunction) {
  try {
    const activity = await userService.getMyActivity(req.auth!.userId);
    res.json(activity);
  } catch (err) {
    next(err);
  }
}

export async function getMyCollection(req: Request, res: Response, next: NextFunction) {
  try {
    const collection = await userService.getMyCollection(req.auth!.userId);
    res.json(collection);
  } catch (err) {
    next(err);
  }
}

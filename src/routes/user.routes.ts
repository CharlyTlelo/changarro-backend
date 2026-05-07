import { Router } from 'express';
import { authMiddleware } from '../middlewares/auth.middleware';
import * as userCtrl from '../controllers/user.controller';

export const userRouter = Router();

userRouter.use(authMiddleware);

userRouter.get('/me', userCtrl.getMyProfile);
userRouter.patch('/me', userCtrl.updateMyProfile);
userRouter.get('/me/stats', userCtrl.getMyStats);
userRouter.get('/me/stamps', userCtrl.getMyStamps);
userRouter.get('/me/activity', userCtrl.getMyActivity);
userRouter.get('/me/collection', userCtrl.getMyCollection);

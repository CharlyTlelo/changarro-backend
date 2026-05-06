import { Router } from 'express';
import { authMiddleware, adminMiddleware } from '../middlewares/auth.middleware';
import * as adminCtrl from '../controllers/admin.controller';

export const adminRouter = Router();

adminRouter.use(authMiddleware, adminMiddleware);

adminRouter.get('/businesses', adminCtrl.getAllBusinesses);
adminRouter.get('/businesses/:id', adminCtrl.getBusinessById);
adminRouter.get('/users', adminCtrl.getAllUsers);

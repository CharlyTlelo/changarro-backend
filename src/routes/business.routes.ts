import { Router } from 'express';
import { authMiddleware } from '../middlewares/auth.middleware';
import * as bizCtrl from '../controllers/business.controller';

export const businessRouter = Router();

businessRouter.get('/', bizCtrl.getPublicBusinesses);

businessRouter.use(authMiddleware);

businessRouter.get('/mine', bizCtrl.getMyBusiness);
businessRouter.patch('/mine', bizCtrl.updateMyBusiness);
businessRouter.put('/mine/menu', bizCtrl.replaceMenu);
businessRouter.put('/mine/promo', bizCtrl.setPromo);
businessRouter.delete('/mine/promo', bizCtrl.deletePromo);
businessRouter.get('/mine/analytics', bizCtrl.getAnalytics);

import { Router } from 'express';
import * as authCtrl from '../controllers/auth.controller';

export const authRouter = Router();

authRouter.post('/register-business', authCtrl.registerBusiness);
authRouter.post('/register', authCtrl.register);
authRouter.post('/login', authCtrl.login);
authRouter.post('/admin/login', authCtrl.adminLogin);

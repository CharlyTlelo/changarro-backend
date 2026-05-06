import jwt from 'jsonwebtoken';
import { AuthPayload } from '../middlewares/auth.middleware';

const JWT_SECRET = process.env.JWT_SECRET || 'changarro-dev-secret-change-in-production';
const EXPIRES_IN = '7d';

export function signToken(payload: AuthPayload): string {
  return jwt.sign(payload, JWT_SECRET, { expiresIn: EXPIRES_IN });
}

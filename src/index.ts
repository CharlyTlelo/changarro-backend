import dotenv from 'dotenv';
dotenv.config();

import express from 'express';
import cors from 'cors';
import mongoose from 'mongoose';
import { authRouter } from './routes/auth.routes';
import { businessRouter } from './routes/business.routes';
import { adminRouter } from './routes/admin.routes';
import { userRouter } from './routes/user.routes';
import { errorHandler } from './middlewares/error.middleware';

const app = express();
const PORT = process.env.PORT || 8080;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/changarro_db';

app.use(cors({ origin: ['http://localhost:4200', 'http://localhost:4000'], credentials: true }));
app.use(express.json({ limit: '5mb' }));

app.use('/api/auth', authRouter);
app.use('/api/businesses', businessRouter);
app.use('/api/users', userRouter);
app.use('/api/admin', adminRouter);

app.get('/api/health', (_req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

app.use(errorHandler);

mongoose.connect(MONGO_URI).then(() => {
  console.log('Connected to MongoDB');
  app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
}).catch(err => {
  console.error('MongoDB connection error:', err);
  process.exit(1);
});

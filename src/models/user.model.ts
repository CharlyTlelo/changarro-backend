import mongoose, { Schema, Document } from 'mongoose';

export interface IUser extends Document {
  email?: string;
  whatsapp?: string;
  phone?: string;
  profilePhotoUrl?: string;
  name: string;
  passwordHash: string;
  role: 'USER' | 'BUSINESS' | 'CUSTOMER' | 'ADMIN';
  businessId: mongoose.Types.ObjectId | null;
  coins: number;
  level: number;
  levelName: string;
  createdAt: Date;
}

const userSchema = new Schema<IUser>({
  email: { type: String, unique: true, sparse: true, index: true, lowercase: true, trim: true },
  whatsapp: { type: String, sparse: true },
  phone: { type: String },
  profilePhotoUrl: { type: String, default: '' },
  name: { type: String, required: true, trim: true },
  passwordHash: { type: String, required: true },
  role: { type: String, enum: ['USER', 'BUSINESS', 'CUSTOMER', 'ADMIN'], default: 'USER' },
  businessId: { type: Schema.Types.ObjectId, ref: 'Business', default: null },
  coins: { type: Number, default: 0 },
  level: { type: Number, default: 1 },
  levelName: { type: String, default: 'Nuevo' },
}, { timestamps: true });

export const User = mongoose.model<IUser>('User', userSchema);

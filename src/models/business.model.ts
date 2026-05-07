import mongoose, { Schema, Document, Types } from 'mongoose';

export interface IMenuItem {
  name: string;
  emoji: string;
  price: string;
  orderCount: number;
}

export interface IPromo {
  title: string;
  label: string;
  validUntil: string;
  note: string;
  bonusCoins: number;
  active: boolean;
}

export interface IDaySchedule {
  isOpen: boolean;
  openTime: string;
  openPeriod: 'AM' | 'PM';
  closeTime: string;
  closePeriod: 'AM' | 'PM';
}

export interface IPaymentMethods {
  efectivo: boolean;
  tarjetas: boolean;
  transferencia: boolean;
}

export interface IBusiness extends Document {
  ownerId: Types.ObjectId;
  name: string;
  description: string;
  address: string;
  neighborhood: string;
  phone: string;
  whatsapp: string;
  instagram: string;
  facebook: string;
  tiktok: string;
  youtube: string;
  categoryId: string;
  paymentMethod: string;
  paymentMethods: IPaymentMethods;
  rating: number;
  reviewCount: number;
  visitCount: number;
  emoji: string;
  color: string;
  tags: string[];
  tag: string;
  priceRange: string;
  schedule: string;
  weeklySchedule: Record<string, IDaySchedule>;
  locationText: string;
  photos: string[];
  verified: boolean;
  trending: boolean;
  nuevo: boolean;
  menuItems: IMenuItem[];
  activePromo: IPromo | null;
  createdAt: Date;
  updatedAt: Date;
}

const menuItemSchema = new Schema<IMenuItem>({
  name: { type: String, required: true },
  emoji: { type: String, default: '🍽️' },
  price: { type: String, required: true },
  orderCount: { type: Number, default: 0 },
}, { _id: true });

const promoSchema = new Schema<IPromo>({
  title: { type: String, required: true },
  label: { type: String, default: '' },
  validUntil: { type: String, default: '' },
  note: { type: String, default: '' },
  bonusCoins: { type: Number, default: 0 },
  active: { type: Boolean, default: true },
}, { _id: true });

const dayScheduleSchema = new Schema<IDaySchedule>({
  isOpen: { type: Boolean, default: false },
  openTime: { type: String, default: '9:00' },
  openPeriod: { type: String, enum: ['AM', 'PM'], default: 'AM' },
  closeTime: { type: String, default: '6:00' },
  closePeriod: { type: String, enum: ['AM', 'PM'], default: 'PM' },
}, { _id: false });

const paymentMethodsSchema = new Schema<IPaymentMethods>({
  efectivo: { type: Boolean, default: true },
  tarjetas: { type: Boolean, default: false },
  transferencia: { type: Boolean, default: false },
}, { _id: false });

const businessSchema = new Schema<IBusiness>({
  ownerId: { type: Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  name: { type: String, required: true },
  description: { type: String, default: '' },
  address: { type: String, default: '' },
  neighborhood: { type: String, default: '' },
  phone: { type: String, default: '' },
  whatsapp: { type: String, default: '' },
  instagram: { type: String, default: '' },
  facebook: { type: String, default: '' },
  tiktok: { type: String, default: '' },
  youtube: { type: String, default: '' },
  categoryId: { type: String, default: '' },
  paymentMethod: { type: String, default: '' },
  paymentMethods: { type: paymentMethodsSchema, default: () => ({ efectivo: true, tarjetas: false, transferencia: false }) },
  rating: { type: Number, default: 0 },
  reviewCount: { type: Number, default: 0 },
  visitCount: { type: Number, default: 0 },
  emoji: { type: String, default: '🏪' },
  color: { type: String, default: '#FF6B35' },
  tags: { type: [String], default: [] },
  tag: { type: String, default: '' },
  priceRange: { type: String, default: '$' },
  schedule: { type: String, default: '' },
  weeklySchedule: {
    type: Map,
    of: dayScheduleSchema,
    default: () => ({
      lunes: { isOpen: true, openTime: '9:00', openPeriod: 'AM', closeTime: '6:00', closePeriod: 'PM' },
      martes: { isOpen: true, openTime: '9:00', openPeriod: 'AM', closeTime: '6:00', closePeriod: 'PM' },
      miercoles: { isOpen: true, openTime: '9:00', openPeriod: 'AM', closeTime: '6:00', closePeriod: 'PM' },
      jueves: { isOpen: true, openTime: '9:00', openPeriod: 'AM', closeTime: '6:00', closePeriod: 'PM' },
      viernes: { isOpen: true, openTime: '9:00', openPeriod: 'AM', closeTime: '6:00', closePeriod: 'PM' },
      sabado: { isOpen: false, openTime: '9:00', openPeriod: 'AM', closeTime: '6:00', closePeriod: 'PM' },
      domingo: { isOpen: false, openTime: '9:00', openPeriod: 'AM', closeTime: '6:00', closePeriod: 'PM' },
    }),
  },
  locationText: { type: String, default: '' },
  photos: { type: [String], default: [] },
  verified: { type: Boolean, default: false },
  trending: { type: Boolean, default: false },
  nuevo: { type: Boolean, default: true },
  menuItems: { type: [menuItemSchema], default: [] },
  activePromo: { type: promoSchema, default: null },
}, { timestamps: true, toJSON: { virtuals: true }, toObject: { virtuals: true } });

businessSchema.virtual('id').get(function () {
  return this._id.toHexString();
});

export const Business = mongoose.model<IBusiness>('Business', businessSchema);

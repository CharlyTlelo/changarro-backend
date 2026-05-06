import mongoose, { Schema, Document, Types } from 'mongoose';

export interface IReview {
  id: string;
  rating: number;
  text: string;
  createdAt: string;
}

export interface IAnalytics extends Document {
  businessId: Types.ObjectId;
  businessName: string;
  visitCount: number;
  reviewCount: number;
  rating: number;
  menuItemCount: number;
  hasActivePromo: boolean;
  isTrending: boolean;
  totalOrders: number;
  topMenuItem: string | null;
  recentReviews: IReview[];
  updatedAt: Date;
}

const reviewSchema = new Schema<IReview>({
  id: String,
  rating: Number,
  text: String,
  createdAt: String,
}, { _id: false });

const analyticsSchema = new Schema<IAnalytics>({
  businessId: { type: Schema.Types.ObjectId, ref: 'Business', required: true, unique: true },
  businessName: { type: String, default: '' },
  visitCount: { type: Number, default: 0 },
  reviewCount: { type: Number, default: 0 },
  rating: { type: Number, default: 0 },
  menuItemCount: { type: Number, default: 0 },
  hasActivePromo: { type: Boolean, default: false },
  isTrending: { type: Boolean, default: false },
  totalOrders: { type: Number, default: 0 },
  topMenuItem: { type: String, default: null },
  recentReviews: { type: [reviewSchema], default: [] },
}, { timestamps: true });

export const Analytics = mongoose.model<IAnalytics>('Analytics', analyticsSchema);

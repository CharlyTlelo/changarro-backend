import mongoose, { Schema, Document } from 'mongoose';

export interface IActivity extends Document {
  userId: mongoose.Types.ObjectId;
  type: 'visit' | 'review' | 'redeem' | 'stamp' | 'coins';
  emoji: string;
  text: string;
  coins: number;
  createdAt: Date;
}

const activitySchema = new Schema<IActivity>({
  userId: { type: Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  type: { type: String, enum: ['visit', 'review', 'redeem', 'stamp', 'coins'], required: true },
  emoji: { type: String, required: true },
  text: { type: String, required: true },
  coins: { type: Number, default: 0 },
}, { timestamps: true });

activitySchema.index({ userId: 1, createdAt: -1 });

export const Activity = mongoose.model<IActivity>('Activity', activitySchema);

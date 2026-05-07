import mongoose, { Schema, Document } from 'mongoose';

export interface IStampDefinition extends Document {
  label: string;
  emoji: string;
  color: string;
  description: string;
  requirement: string;
  order: number;
}

export interface IUserStamp extends Document {
  userId: mongoose.Types.ObjectId;
  stampId: mongoose.Types.ObjectId;
  earnedAt: Date;
}

const stampDefinitionSchema = new Schema<IStampDefinition>({
  label: { type: String, required: true },
  emoji: { type: String, required: true },
  color: { type: String, required: true },
  description: { type: String, default: '' },
  requirement: { type: String, default: '' },
  order: { type: Number, default: 0 },
}, { timestamps: true });

const userStampSchema = new Schema<IUserStamp>({
  userId: { type: Schema.Types.ObjectId, ref: 'User', required: true, index: true },
  stampId: { type: Schema.Types.ObjectId, ref: 'StampDefinition', required: true },
  earnedAt: { type: Date, default: Date.now },
});

userStampSchema.index({ userId: 1, stampId: 1 }, { unique: true });

export const StampDefinition = mongoose.model<IStampDefinition>('StampDefinition', stampDefinitionSchema);
export const UserStamp = mongoose.model<IUserStamp>('UserStamp', userStampSchema);

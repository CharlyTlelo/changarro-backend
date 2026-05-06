import dotenv from 'dotenv';
dotenv.config();

import mongoose from 'mongoose';
import bcrypt from 'bcrypt';
import { User } from '../models/user.model';
import { Business } from '../models/business.model';
import { Analytics } from '../models/analytics.model';

const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/changarro_db';

async function seed() {
  await mongoose.connect(MONGO_URI);
  console.log('Connected to MongoDB for seeding...');

  // Clear existing data
  await User.deleteMany({});
  await Business.deleteMany({});
  await Analytics.deleteMany({});

  // Create admin
  const adminHash = await bcrypt.hash('Admin123', 10);
  await User.create({
    name: 'Admin Changarro',
    email: 'admin@changarro.mx',
    passwordHash: adminHash,
    role: 'ADMIN',
  });

  // Create business owner
  const bizHash = await bcrypt.hash('Demo123', 10);
  const bizUser = await User.create({
    name: 'Carlos Demo',
    email: 'demo@changarro.mx',
    phone: '5551234567',
    passwordHash: bizHash,
    role: 'BUSINESS',
  });

  // Create business with menu and promo
  const business = await Business.create({
    ownerId: bizUser._id,
    name: 'Tacos El Paisa',
    description: 'Los mejores tacos al pastor de la colonia. Tortilla hecha a mano.',
    address: 'Av. Insurgentes Sur 1234, CDMX',
    neighborhood: 'Roma Norte',
    phone: '5551234567',
    categoryId: 'food',
    emoji: '🌮',
    color: '#FF6B35',
    tags: ['tacos', 'pastor', 'comida mexicana'],
    tag: 'Comida',
    priceRange: '$$',
    schedule: 'Lun-Sáb 10:00-22:00',
    locationText: 'https://maps.google.com/?q=19.4,-99.17',
    verified: true,
    trending: true,
    menuItems: [
      { name: 'Taco al Pastor', emoji: '🌮', price: '$25', orderCount: 142 },
      { name: 'Taco de Suadero', emoji: '🥩', price: '$28', orderCount: 87 },
      { name: 'Quesadilla de Chicharrón', emoji: '🧀', price: '$35', orderCount: 63 },
      { name: 'Agua de Horchata', emoji: '🥤', price: '$20', orderCount: 95 },
      { name: 'Gringa', emoji: '🫔', price: '$45', orderCount: 51 },
    ],
    activePromo: {
      title: '2x1 en Gringas',
      label: 'PROMO',
      validUntil: '2026-06-30',
      note: 'Solo martes y jueves',
      bonusCoins: 10,
      active: true,
    },
  });

  // Create analytics
  await Analytics.create({
    businessId: business._id,
    businessName: business.name,
    visitCount: 1247,
    reviewCount: 89,
    rating: 4.6,
    menuItemCount: 5,
    hasActivePromo: true,
    isTrending: true,
    totalOrders: 438,
    topMenuItem: 'Taco al Pastor',
    recentReviews: [
      { id: '1', rating: 5, text: 'Los mejores tacos que he probado!', createdAt: '2026-05-01T12:00:00Z' },
      { id: '2', rating: 4, text: 'Muy buenos, aunque tuve que esperar un poco', createdAt: '2026-04-28T18:30:00Z' },
      { id: '3', rating: 5, text: 'La salsa verde es increíble', createdAt: '2026-04-25T20:15:00Z' },
    ],
  });

  // Create a customer
  const custHash = await bcrypt.hash('Cliente123', 10);
  await User.create({
    name: 'María López',
    email: 'maria@gmail.com',
    passwordHash: custHash,
    role: 'CUSTOMER',
    coins: 50,
    level: 2,
    levelName: 'Explorador',
  });

  console.log('Seed completed!');
  console.log('---');
  console.log('Usuarios creados:');
  console.log('  Admin:    admin@changarro.mx / Admin123');
  console.log('  Negocio:  demo@changarro.mx / Demo123');
  console.log('  Cliente:  maria@gmail.com / Cliente123');
  console.log('---');

  await mongoose.disconnect();
}

seed().catch(err => {
  console.error('Seed failed:', err);
  process.exit(1);
});

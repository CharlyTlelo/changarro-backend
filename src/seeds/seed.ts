import dotenv from 'dotenv';
dotenv.config();

import mongoose from 'mongoose';
import bcrypt from 'bcrypt';
import { User } from '../models/user.model';
import { Business } from '../models/business.model';
import { Analytics } from '../models/analytics.model';
import { StampDefinition, UserStamp } from '../models/stamp.model';
import { Activity } from '../models/activity.model';
import { Favorite } from '../models/favorite.model';

const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/changarro_db';

async function seed() {
  await mongoose.connect(MONGO_URI);
  console.log('Connected to MongoDB for seeding...');

  await User.deleteMany({});
  await Business.deleteMany({});
  await Analytics.deleteMany({});
  await StampDefinition.deleteMany({});
  await UserStamp.deleteMany({});
  await Activity.deleteMany({});
  await Favorite.deleteMany({});

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

  // Create business
  const business = await Business.create({
    ownerId: bizUser._id,
    name: 'Tacos El Paisa',
    description: 'Los mejores tacos al pastor de la colonia. Tortilla hecha a mano.',
    address: 'Av. Insurgentes Sur 1234, CDMX',
    neighborhood: 'Roma Norte',
    phone: '5551234567',
    whatsapp: '55 1234 5678',
    facebook: 'facebook.com/tacoselpaisa',
    tiktok: '@tacoselpaisa',
    instagram: '@tacoselpaisa',
    youtube: '',
    categoryId: 'comida',
    emoji: '🌮',
    color: '#FF6B35',
    tags: ['tacos', 'pastor', 'comida mexicana'],
    tag: 'Comida',
    priceRange: '$$',
    schedule: 'Lun-Sáb 10:00-22:00',
    paymentMethods: { efectivo: true, tarjetas: true, transferencia: false },
    weeklySchedule: {
      lunes: { isOpen: true, openTime: '10:00', openPeriod: 'AM', closeTime: '10:00', closePeriod: 'PM' },
      martes: { isOpen: true, openTime: '10:00', openPeriod: 'AM', closeTime: '10:00', closePeriod: 'PM' },
      miercoles: { isOpen: true, openTime: '10:00', openPeriod: 'AM', closeTime: '10:00', closePeriod: 'PM' },
      jueves: { isOpen: true, openTime: '10:00', openPeriod: 'AM', closeTime: '10:00', closePeriod: 'PM' },
      viernes: { isOpen: true, openTime: '10:00', openPeriod: 'AM', closeTime: '10:00', closePeriod: 'PM' },
      sabado: { isOpen: true, openTime: '11:00', openPeriod: 'AM', closeTime: '8:00', closePeriod: 'PM' },
      domingo: { isOpen: false, openTime: '9:00', openPeriod: 'AM', closeTime: '6:00', closePeriod: 'PM' },
    },
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

  // Extra businesses for collection/favorites
  const extraBizData = [
    { name: 'Café Avellaneda', emoji: '☕', color: '#C68A52', categoryId: 'comida' },
    { name: 'Estética Rocío', emoji: '💇', color: '#8FC4DC', categoryId: 'servicios' },
    { name: 'Panadería Sol', emoji: '🥖', color: '#E8B860', categoryId: 'comida' },
    { name: 'Mercería Lupita', emoji: '🧵', color: '#A8D08B', categoryId: 'tienda' },
    { name: 'Florería Camelia', emoji: '💐', color: '#F5A8B8', categoryId: 'tienda' },
  ];
  const extraBizzes = [];
  for (const eb of extraBizData) {
    const b = await Business.create({
      ownerId: bizUser._id,
      name: eb.name,
      emoji: eb.emoji,
      color: eb.color,
      categoryId: eb.categoryId,
      address: 'CDMX',
      neighborhood: 'Roma Norte',
      description: `${eb.name} del barrio`,
    });
    extraBizzes.push(b);
  }

  // Analytics
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

  // Stamp definitions (8 sellos del barrio)
  const stampDefs = await StampDefinition.insertMany([
    { label: 'Taquero', emoji: '🌮', color: '#DD4D2A', description: 'Visita 5 taquerías', requirement: '5 taquerías', order: 1 },
    { label: 'Cafeicón', emoji: '☕', color: '#C68A52', description: 'Visita 3 cafeterías', requirement: '3 cafeterías', order: 2 },
    { label: 'Bohemio', emoji: '🎸', color: '#7A3FA8', description: 'Visita 3 bares o cantinas', requirement: '3 bares', order: 3 },
    { label: 'Mañanero', emoji: '🌅', color: '#F5B92E', description: 'Visita antes de las 9am', requirement: '5 visitas tempranas', order: 4 },
    { label: 'Vecino', emoji: '🏘️', color: '#4A8A3A', description: 'Visita 10 negocios distintos', requirement: '10 negocios', order: 5 },
    { label: 'Madrugador', emoji: '🐓', color: '#2B6FA0', description: 'Primera visita del día', requirement: '10 primeras visitas', order: 6 },
    { label: 'Curandero', emoji: '🌿', color: '#7A3FA8', description: 'Visita 3 farmacias o salud', requirement: '3 salud', order: 7 },
    { label: 'Bailarín', emoji: '💃', color: '#E8628E', description: 'Visita 3 lugares de diversión', requirement: '3 diversión', order: 8 },
  ]);

  // Customer user with real data
  const custHash = await bcrypt.hash('Cliente123', 10);
  const customer = await User.create({
    name: 'María López',
    email: 'maria@gmail.com',
    whatsapp: '5587654321',
    phone: '5587654321',
    passwordHash: custHash,
    role: 'CUSTOMER',
    coins: 740,
    level: 4,
    levelName: 'Embajadora',
  });

  // Customer earned stamps (4 of 8)
  await UserStamp.insertMany([
    { userId: customer._id, stampId: stampDefs[0]._id, earnedAt: new Date('2026-04-01') },
    { userId: customer._id, stampId: stampDefs[1]._id, earnedAt: new Date('2026-04-10') },
    { userId: customer._id, stampId: stampDefs[2]._id, earnedAt: new Date('2026-04-15') },
    { userId: customer._id, stampId: stampDefs[3]._id, earnedAt: new Date('2026-04-28') },
  ]);

  // Customer favorites (collection)
  await Favorite.insertMany([
    { userId: customer._id, businessId: business._id },
    ...extraBizzes.map(b => ({ userId: customer._id, businessId: b._id })),
  ]);

  // Customer activity
  const now = Date.now();
  await Activity.insertMany([
    { userId: customer._id, type: 'visit', emoji: '🌮', text: 'Visitaste Tacos El Paisa', coins: 20, createdAt: new Date(now - 2 * 3_600_000) },
    { userId: customer._id, type: 'review', emoji: '⭐', text: 'Reseña en Café Avellaneda', coins: 50, createdAt: new Date(now - 24 * 3_600_000) },
    { userId: customer._id, type: 'redeem', emoji: '🥖', text: 'Canjeaste en Panadería Sol', coins: -400, createdAt: new Date(now - 3 * 86_400_000) },
    { userId: customer._id, type: 'visit', emoji: '💇', text: 'Visitaste Estética Rocío', coins: 20, createdAt: new Date(now - 5 * 86_400_000) },
    { userId: customer._id, type: 'stamp', emoji: '🏆', text: 'Sello "Mañanero" obtenido', coins: 100, createdAt: new Date(now - 7 * 86_400_000) },
    { userId: customer._id, type: 'visit', emoji: '💐', text: 'Visitaste Florería Camelia', coins: 20, createdAt: new Date(now - 10 * 86_400_000) },
    { userId: customer._id, type: 'review', emoji: '⭐', text: 'Reseña en Mercería Lupita', coins: 50, createdAt: new Date(now - 12 * 86_400_000) },
    { userId: customer._id, type: 'visit', emoji: '🧵', text: 'Visitaste Mercería Lupita', coins: 20, createdAt: new Date(now - 14 * 86_400_000) },
  ]);

  console.log('Seed completed!');
  console.log('---');
  console.log('Usuarios creados:');
  console.log('  Admin:    admin@changarro.mx / Admin123');
  console.log('  Negocio:  demo@changarro.mx / Demo123');
  console.log('  Cliente:  maria@gmail.com / Cliente123');
  console.log('---');
  console.log(`Stamps: ${stampDefs.length} definidos, 4 ganados por María`);
  console.log(`Activities: 8 items para María`);
  console.log(`Favorites: ${1 + extraBizzes.length} para María`);

  await mongoose.disconnect();
}

seed().catch(err => {
  console.error('Seed failed:', err);
  process.exit(1);
});

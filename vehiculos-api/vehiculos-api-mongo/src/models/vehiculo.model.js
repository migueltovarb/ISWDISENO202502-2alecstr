const mongoose = require('mongoose');

const VehiculoSchema = new mongoose.Schema({
  marca: { type: String, required: true },
  modelo: { type: String, required: true },
  anio: { type: Number, required: true },
  placa: { type: String, required: true }
});

module.exports = mongoose.model('Vehiculo', VehiculoSchema);

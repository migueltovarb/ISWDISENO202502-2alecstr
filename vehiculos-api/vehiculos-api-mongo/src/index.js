const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const vehiculosRoutes = require('./routes/vehiculos.routes');

const app = express();
const PORT = 3001;

// Middleware
app.use(cors());
app.use(express.json());

// Conexión a MongoDB
mongoose.connect('mongodb+srv://johancastro_db_user:<db_password>@cluster0.nh1ncox.mongodb.net/?appName=Cluster0')
  .then(() => console.log('MongoDB conectado correctamente'))
  .catch(err => console.error('Error al conectar MongoDB:', err));

// Rutas
app.use('/vehiculos', vehiculosRoutes);

// Ruta no encontrada
app.use((req, res) => {
  res.status(404).json({ mensaje: 'Ruta no encontrada' });
});

app.listen(PORT, () => {
  console.log(`Servidor escuchando en http://localhost:${PORT}`);
});

const ok = (res, message = "Solicitud procesada correctamente") => {
  res.status(200).json({ message });
};

const created = (res, message = "Creado correctamente") => {
  res.status(201).json({ message });
};

const badRequest = (res, fields) => {
  res.status(400).json({ fields });
};

const unauthorized = (res, message) => {
  res.status(401).json({ message });
};

const forbidden = (res, message, code) => {
  res.status(403).json({ message });
};

const notFound = (res, message = "Recurso no encontrado") => {
  res.status(404).json({ message });
};

const serviceUnavailable = (res, message = "No hay servidores disponibles") => {
  res.status(503).json({ message });
};

module.exports = { ok, created, badRequest, unauthorized, forbidden, notFound, serviceUnavailable };

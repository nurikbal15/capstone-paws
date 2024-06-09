const express = require("express");
const jadwalMakanRoutes = require("./routes/jadwalMakan.route");
const jadwalVitaminRoutes = require("./routes/jadwalVitamin.route");
const jadwalAktifitasRoutes = require("./routes/jadwalAktifitas.route");
const peliharaanRoutes = require("./routes/peliharaan.route");
const jadwalTemuRoutes = require("./routes/jadwalTemu.route");
const axios = require('axios');
const { body, validationResult } = require("express-validator");
const { authentificateToken } = require("./middlewares/auth");
const API_KEY = '5a8109e0575d4a13ae6a222fbae07410';
const app = express();

app.use(express.json());

app.use("/jadwalmakans", jadwalMakanRoutes);
app.use("/jadwalAktifitass", jadwalAktifitasRoutes);
app.use("/jadwalvitamins", jadwalVitaminRoutes);
app.use("/peliharaans", peliharaanRoutes);
app.use("/jadwaltemus", jadwalTemuRoutes);

app.get('/api/tips-for-cat-and-dog-care', async (req, res) => {
    const url = `https://newsapi.org/v2/everything?q=Tips%20for%20Cat%20and%20Dog%20Care&sortBy=popularity&apiKey=${API_KEY}`;
    
    try {
      const response = await axios.get(url);
      const articles = response.data.articles.filter(article => article.content !== "[Removed]");
      res.status(200).json(articles);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Error fetching data from NewsAPI' });
    }
  });

app.listen(3000);

const { auth } = require('../configs/firebase');
const prisma = require('../configs/prismaClient');

const authentificateToken = async (req, res, next) => {
  try {
    const bearerHeader = req.headers["authorization"];
    if (typeof bearerHeader !== "undefined") {
      const token = bearerHeader.split(" ")[1];
      if (!token) return res.status(401).json({ message: "Token required" });

      const decodedToken = await auth.verifyIdToken(token);

      // console.log(decodedToken);
      
      const user = await prisma.user.findUnique({
        where: {
          userUID: decodedToken.uid,
        },
      });

      // If the user does not exist, create a new user
      if (!user) {
        await prisma.user.create({
          data: {
            userUID: decodedToken.uid,

          },
        });
      }

      // Attach the user to the request object for further use
      req.user = user;

      next();
    } else {
      res.status(401).json({ message: "Unauthorized" });
    }
  } catch (error) {
    console.error(error.message);
    res.status(403).json({ message: "Forbidden" });
  }
};

module.exports = { authentificateToken };
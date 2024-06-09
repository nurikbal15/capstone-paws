const admin = require("firebase-admin");
const { getAuth } = require("firebase-admin/auth");

const app = admin.initializeApp({
  credential: admin.credential.cert("serviceAccount.json"),
});

const auth = getAuth();

module.exports = { auth };

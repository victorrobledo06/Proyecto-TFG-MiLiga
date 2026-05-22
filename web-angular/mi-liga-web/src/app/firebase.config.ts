import { initializeApp, getApps } from "firebase/app";
import { getDatabase } from "firebase/database";

const firebaseConfig = {
  apiKey: "AIzaSyAp4wuMZl4M742MmDLGEBCQjld2ClL_C1Q",
  authDomain: "miliga-c210e.firebaseapp.com",
  databaseURL: "https://miliga-c210e-default-rtdb.firebaseio.com/",
  projectId: "miliga-c210e",
  appId: "1:584106823197:web:e2c9b75ea065011b7800f4"
};

const firebaseApp = getApps().length ? getApps()[0] : initializeApp(firebaseConfig);
export const db = getDatabase(firebaseApp);
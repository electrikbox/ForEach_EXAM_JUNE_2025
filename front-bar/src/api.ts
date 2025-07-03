import axios from 'axios';

const auth = axios.create({
  baseURL: 'http://localhost:8000/auth',
  withCredentials: true,
});

const api = axios.create({
  baseURL: 'http://localhost:8000/api',
  withCredentials: true,
});

export async function login(emailUtilisateur: string, motDePasse: string) {
  return auth.post('/login', { emailUtilisateur, motDePasse });
}

export async function register(emailUtilisateur: string, motDePasse: string) {
  return auth.post('/register', { emailUtilisateur, motDePasse });
}

export async function getUser() {
  return auth.get('/me', { withCredentials: true });
}

export async function getCocktails() {
  return api.get('/cocktails', { withCredentials: true })
}

export async function getCocktailDetails(id: number) {
  return api.get(`/cocktails/${id}`, { withCredentials: true })
}

export async function getCategories() {
  return api.get('/categories', { withCredentials: true })
}

export async function logout() {
  return auth.post('/logout', {}, { withCredentials: true });
}

export async function creerCommande(commandeData: any) {
  return api.post('/commandes', commandeData, { withCredentials: true });
}

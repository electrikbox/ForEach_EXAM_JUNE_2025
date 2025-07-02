import axios from 'axios';

const auth = axios.create({
  baseURL: 'http://localhost:8000',
  withCredentials: true,
});

export async function login(emailUtilisateur: string, motDePasse: string) {
  return auth.post('/auth/login', { emailUtilisateur, motDePasse });
}

export async function register(emailUtilisateur: string, motDePasse: string) {
  return auth.post('/auth/register', { emailUtilisateur, motDePasse });
}

export default auth; 
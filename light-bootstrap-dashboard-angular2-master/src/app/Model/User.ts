export enum RoleEnum {
  ROLE_ADMINISTRATEUR = 'ROLE_ADMINISTRATEUR',
  ROLE_SUPER_ADMIN = 'ROLE_SUPER_ADMIN',
  ROLE_ADMIN_COMMERCIAL = 'ROLE_ADMIN_COMMERCIAL',
  ROLE_ADMIN_TECHNIQUE = 'ROLE_ADMIN_TECHNIQUE',
  ROLE_COMMERCIAL = 'ROLE_COMMERCIAL',
  ROLE_TECHNIQUE = 'ROLE_TECHNIQUE'
}

export interface User {
  id?: number; // optionnel car généré côté backend
  firstname: string;
  lastname: string;
  email: string;
  password?: string; // optionnel pour ne pas exposer côté front dans certaines requêtes
  sex: string;
  phoneNumber: string;
  dateOfBirth: string; // format ISO 'yyyy-MM-dd'
  role: RoleEnum;
  verificationToken?: string;
  verified?: boolean;
  profilePicture: String ;
}

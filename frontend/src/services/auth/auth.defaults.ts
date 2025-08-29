// import type { MockUser } from './auth.types'

const prefix = '/auth'

export const AUTH_API = {
  login: `${prefix}/login`,
  register: `${prefix}/register`,
  logout: `${prefix}/logout`,
}

// export const mockUsers: MockUser[] = [
//   {
//     id: '1',
//     name: 'Administrador',
//     email: 'admin@demo.com',
//     password: 'admin123',
//     role: 'admin',
//     avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
//   },
//   {
//     id: '2',
//     name: 'Usuario Demo',
//     email: 'user@demo.com',
//     password: 'user123',
//     role: 'user',
//     avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
//   },
//   {
//     id: '3',
//     name: 'Juan Pérez',
//     email: 'juan@demo.com',
//     password: 'juan123',
//     role: 'user',
//     avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
//   },
// ]

import type { User } from '@/types/auth.types'

export interface MockUser extends User {
    password: string
}

export const mockUsers: MockUser[] = [
    {
        id: '1',
        name: 'Administrador',
        email: 'admin@demo.com',
        password: 'admin123',
        role: 'ADMIN',
        avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
    },
    {
        id: '2',
        name: 'Usuario Demo',
        email: 'user@demo.com',
        password: 'user123',
        role: 'USER',
        avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
    },
    {
        id: '3',
        name: 'Juan Pérez',
        email: 'juan@demo.com',
        password: 'juan123',
        role: 'USER',
        avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
    },
]

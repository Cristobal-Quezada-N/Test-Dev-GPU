interface MockUser {
  id: string
  name: string
  email: string
  password: string
  role: 'ADMIN' | 'USER'
  avatar?: string
}

export type { MockUser }

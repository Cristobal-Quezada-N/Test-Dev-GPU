interface MockUser {
  id: string
  name: string
  email: string
  password: string
  role: 'admin' | 'user'
  avatar?: string
}

export type { MockUser }

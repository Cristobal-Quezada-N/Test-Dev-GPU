export interface Header {
  title: string
  key: string
  align?: 'start' | 'center' | 'end'
  sortable?: boolean
  width?: string | number
}

export interface Item {
  id: number
  name: string
  stock: number
  available: boolean
}

export interface ItemTableProps {
  // Contenedor
  fluid?: boolean
  containerClass?: string
  elevation?: number
  // Titulo
  title?: string
  // Tabla
  headers: Header[]
  items: Item[]
  // Available
  availableTrueText?: string
  availableFalseText?: string
  availableTrueColor?: string
  availableFalseColor?: string
}

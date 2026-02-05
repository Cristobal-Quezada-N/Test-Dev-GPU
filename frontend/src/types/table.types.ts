export interface Header {
  title: string
  key: string
  align?: 'start' | 'center' | 'end'
  sortable?: boolean
  width?: string | number
}

export interface ItemTableProps<T = Record<string, unknown>> {
  // Contenedor
  fluid?: boolean
  containerClass?: string
  elevation?: number
  // Titulo
  title?: string
  // Tabla
  headers: Header[]
  items: T[]
  // Available
  availableTrueText?: string
  availableFalseText?: string
  availableTrueColor?: string
  availableFalseColor?: string
}

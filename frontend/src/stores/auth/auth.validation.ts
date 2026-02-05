export type ValidationRule = (v: string) => boolean | string

// Validation rules
export const authValidation = {
  // Email rules
  emailRules: [
    (v: string) => !!v || 'El correo electrónico es requerido',
    (v: string) => /.+@.+\..+/.test(v) || 'El correo electrónico debe ser válido',
  ] as ValidationRule[],

  // Password rules
  passwordRules: [
    (v: string) => !!v || 'La contraseña es requerida',
    (v: string) => v.length >= 6 || 'La contraseña debe tener al menos 6 caracteres',
  ] as ValidationRule[],

  // Password confirm
  passwordConfirm: (password: string): ValidationRule[] => [
    (v: string) => !!v || 'Confirma tu contraseña',
    (v: string) => v === password || 'Las constraseñas no coinciden',
  ],

  // Custom Validators
  minLength: (min: number) => (v: string) =>
    v.length >= min || `Debe tener al menos ${min} caracteres`,

  maxLength: (max: number) => (v: string) =>
    v.length <= max || `Debe tener como máximo  ${max} caracteres`,

  required: (fieldName: string = 'Este campo') => (v: string) =>
    !!v || `${fieldName} es requerido`

}

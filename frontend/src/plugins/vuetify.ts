/**
 * plugins/vuetify.ts
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Composables
import { createVuetify } from 'vuetify'
// Styles
import '@mdi/font/css/materialdesignicons.css'

import 'vuetify/styles'

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          // Primary colors
          'primary': '#00A499', // PANTONE 3272 C
          'primary-lighten-1': '#24BEB3',
          'primary-lighten-2': '#5ACDC4',
          'primary-lighten-3': '#91DDD7',
          'primary-lighten-4': '#C8EDEA',
          'primary-lighten-5': '#E3F6F4',
          'primary-darken-1': '#00B1A3',
          'primary-darken-2': '#09B8AB',
          'primary-darken-3': '#3FC6BC',
          'primary-darken-4': '#76D5CD',

          // Secondary colors
          'secondary': '#EA7600', // PANTONE 716 C
          'secondary-lighten-1': '#F58A38',
          'secondary-lighten-2': '#FDA666',
          'secondary-lighten-3': '#FFC395',
          'secondary-lighten-4': '#FFE1C8',
          'secondary-lighten-5': '#FFF0E3',
          'secondary-darken-1': '#D76600',
          'secondary-darken-2': '#EE7B20',
          'secondary-darken-3': '#F7964E',
          'secondary-darken-4': '#FFB57D',

          // Neutral colors
          'surface': '#FFFFFF',
          'background': '#F8F9FA',
          'surface-variant': '#F5F5F5',

          // Text colors
          'on-primary': '#FFFFFF',
          'on-secondary': '#FFFFFF',
          'on-surface': '#394049', // PANTONE 432 C
          'on-surface-variant': '#616971',

          // Status colors
          'success': '#00A499',
          'warning': '#EAAA00', // PANTONE 124 C
          'error': '#C8102E', // PANTONE 186 C
          'info': '#498BCA', // PANTONE 279 C

          // Additional colors
          'accent': '#8C4799', // PANTONE 258 C
          'accent-lighten-1': '#9E63A6',
          'accent-lighten-2': '#B687BB',
          'accent-lighten-3': '#CEACD1',
          'accent-lighten-4': '#E7D5E8',
          'accent-lighten-5': '#F3EAF3',
        },
      },
    },
  },
})

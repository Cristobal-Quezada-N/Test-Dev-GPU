/**
 * main.ts
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Composables
import { createApp } from 'vue'

// Plugins
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'

// Styles
import 'unfonts.css'
import { devBypass } from './plugins/dev-auth'

const app = createApp(App)

registerPlugins(app)

// Development bypass
devBypass()

app.mount('#app')

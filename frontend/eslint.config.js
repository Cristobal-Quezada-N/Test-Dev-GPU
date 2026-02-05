import tsPlugin from '@typescript-eslint/eslint-plugin'
import tsParser from '@typescript-eslint/parser'
import vuetify from 'eslint-config-vuetify'
import unusedImports from 'eslint-plugin-unused-imports'
import pluginVue from 'eslint-plugin-vue'
import vueParser from 'vue-eslint-parser'
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting'
import { defineConfigWithVueTs, vueTsConfigs } from '@vue/eslint-config-typescript'
import pluginUnicorn from 'eslint-plugin-unicorn'

export default defineConfigWithVueTs(
  vuetify(),
  ...pluginVue.configs['flat/recommended'],
  vueTsConfigs.recommended,
  skipFormatting,
  {
    files: ['**/*.{js,ts,mts,tsx,vue}'],
    languageOptions: {
      parser: vueParser,
      parserOptions: {
        parser: tsParser,
        ecmaVersion: 'latest',
        sourceType: 'module',
        extraFileExtensions: ['.vue'],
      },
    },
    plugins: {
      '@typescript-eslint': tsPlugin,
      'unused-imports': unusedImports,
      'unicorn': pluginUnicorn
    },
    rules: {
      'import/no-duplicates': 'off',
      'no-duplicate-imports': ['warn', { allowSeparateTypeImports: true }],

      '@typescript-eslint/consistent-type-imports': ['warn', { fixStyle: 'inline-type-imports' }],

      'unused-imports/no-unused-imports': 'warn',

      '@typescript-eslint/no-unused-vars': [
        'warn',
        { argsIgnorePattern: '^_', varsIgnorePattern: '^_' },
      ],

      'vue/multi-word-component-names': 'warn',
      'vue/attributes-order': 'warn',
      'unicorn/no-empty-file': 'warn',
    },
  },
  {
    ignores: ['dist/**', 'node_modules/**'],
  },
)

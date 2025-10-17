INSERT INTO App_user (id, role_id, status_id, email, password) VALUES

  -- ========== Admin User ==========
  --     email: admin@demo.test
  --     password: admin1234
  ('69ab8cf7-54de-469d-9602-6d89bec8280e', 1, 2, 'admin@demo.test', '$2a$10$TmCOdUVoqQQPTUvqikbM.uYSQf0rpTJbHsj82SBXfGVREJTWKpr0i'),

  -- ========== User (ACTIVE) ==========
  --     email: user@demo.test
  --     password: user1234
  ('5604d67c-a015-4f6b-9849-931c66ff95a0', 2, 2,  'user@demo.test', '$2a$10$wk3oeMBZpd7pj6MZabYyQ..HxrA/FuZVaTPF1a7655M.r8CkVxYUO'),

  -- ========== USER (PENDDING) ==========
  --     email: test@demo.test
  --     password: test1234
  ('e0428af7-8ce6-4791-b6ee-b1367afa6841', 2, 1,  'test@demo.test', '$2a$10$ELKDfio4ybSuN6taPzrti.JjW0mztx3nc8sc2.QCoqkzq/sSBNw6W');

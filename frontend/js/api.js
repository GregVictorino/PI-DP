const API_BASE = 'http://localhost:8080/api';

async function apiRequest(method, endpoint, body = null) {
  const options = {
    method,
    headers: { 'Content-Type': 'application/json' }
  };
  if (body) options.body = JSON.stringify(body);

  const res = await fetch(`${API_BASE}${endpoint}`, options);

  if (res.status === 204) return null;

  const data = await res.json();

  if (!res.ok) {
    throw new Error(data.erro || JSON.stringify(data));
  }

  return data;
}

const api = {
  // Livros
  getLivros:     (titulo) => apiRequest('GET', titulo ? `/livros?titulo=${encodeURIComponent(titulo)}` : '/livros'),
  getLivro:      (id)     => apiRequest('GET', `/livros/${id}`),
  createLivro:   (data)   => apiRequest('POST', '/livros', data),
  updateLivro:   (id, d)  => apiRequest('PUT', `/livros/${id}`, d),
  deleteLivro:   (id)     => apiRequest('DELETE', `/livros/${id}`),

  // Usuários
  getUsuarios:   ()       => apiRequest('GET', '/usuarios'),
  createUsuario: (data)   => apiRequest('POST', '/usuarios', data),
  updateUsuario: (id, d)  => apiRequest('PUT', `/usuarios/${id}`, d),
  deleteUsuario: (id)     => apiRequest('DELETE', `/usuarios/${id}`),
  login:         (data)   => apiRequest('POST', '/usuarios/login', data),

  // Locações
  getLocacoes:       ()   => apiRequest('GET', '/locacoes'),
  getLocacoesAtivas: ()   => apiRequest('GET', '/locacoes/ativas'),
  createLocacao:   (data) => apiRequest('POST', '/locacoes', data),
  devolverLocacao: (id)   => apiRequest('PUT', `/locacoes/${id}/devolver`),
  deleteLocacao:   (id)   => apiRequest('DELETE', `/locacoes/${id}`),
};

const API_BASE = '/api';

async function apiRequest(method, endpoint, body = null) {
  // Envia X-User-Id automaticamente quando o usuário está logado
  const usuario = JSON.parse(sessionStorage.getItem('usuario') || 'null');

  const options = {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...(usuario ? { 'X-User-Id': String(usuario.id) } : {})
    }
  };
  if (body) options.body = JSON.stringify(body);

  const res = await fetch(`${API_BASE}${endpoint}`, options);

  if (res.status === 204) return null;

  const data = await res.json();

  if (!res.ok) {
    throw new Error(data.erro || data.message || JSON.stringify(data));
  }

  return data;
}

const api = {
  // ── Livros ──────────────────────────────────────────────
  // filtros: { busca, genero, disponivel }
  getLivros: (filtros = {}) => {
    const q = new URLSearchParams();
    if (filtros.busca)                                       q.append('busca', filtros.busca);
    if (filtros.genero)                                      q.append('genero', filtros.genero);
    if (filtros.disponivel !== undefined && filtros.disponivel !== '') q.append('disponivel', filtros.disponivel);
    const qs = q.toString();
    return apiRequest('GET', '/livros' + (qs ? '?' + qs : ''));
  },
  getLivro:    (id)     => apiRequest('GET',    `/livros/${id}`),
  createLivro: (data)   => apiRequest('POST',   '/livros', data),
  updateLivro: (id, d)  => apiRequest('PUT',    `/livros/${id}`, d),
  deleteLivro: (id)     => apiRequest('DELETE', `/livros/${id}`),

  // ── Usuários ─────────────────────────────────────────────
  getUsuarios:   ()      => apiRequest('GET',    '/usuarios'),
  createUsuario: (data)  => apiRequest('POST',   '/usuarios', data),
  updateUsuario: (id, d) => apiRequest('PUT',    `/usuarios/${id}`, d),
  deleteUsuario: (id)    => apiRequest('DELETE', `/usuarios/${id}`),
  login:         (data)  => apiRequest('POST',   '/usuarios/login', data),
  resetarSenha:  (data)  => apiRequest('PUT',    '/usuarios/resetar-senha', data),

  // ── Locações ─────────────────────────────────────────────
  getLocacoes:          ()   => apiRequest('GET',    '/locacoes'),
  getLocacoesAtivas:    ()   => apiRequest('GET',    '/locacoes/ativas'),
  getLocacoesPorUsuario:(id) => apiRequest('GET',    `/locacoes/usuario/${id}`),
  createLocacao:       (data)=> apiRequest('POST',   '/locacoes', data),
  aprovarLocacao:       (id) => apiRequest('PUT',    `/locacoes/${id}/aprovar`),
  rejeitarLocacao:      (id) => apiRequest('PUT',    `/locacoes/${id}/rejeitar`),
  devolverLocacao:      (id) => apiRequest('PUT',    `/locacoes/${id}/devolver`),
  deleteLocacao:        (id) => apiRequest('DELETE', `/locacoes/${id}`),

  // ── Dashboard ────────────────────────────────────────────
  getDashboardResumo: () => apiRequest('GET', '/dashboard/resumo'),
};

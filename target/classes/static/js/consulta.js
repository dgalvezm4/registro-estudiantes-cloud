const rows = document.querySelector('#studentRows');
const search = document.querySelector('#searchInput');
const refresh = document.querySelector('#refreshButton');
const count = document.querySelector('#resultCount');
const lastUpdate = document.querySelector('#lastUpdate');
const tableAlert = document.querySelector('#tableAlert');
let timer;

async function cargar() {
  refresh.disabled = true; refresh.textContent = 'Cargando...'; tableAlert.hidden = true;
  try {
    const response = await fetch(`/api/estudiantes?q=${encodeURIComponent(search.value.trim())}`, {cache:'no-store'});
    if (!response.ok) throw new Error('No fue posible consultar el Web Service');
    const data = await response.json(); render(data);
    lastUpdate.textContent = `Actualizado: ${new Date().toLocaleTimeString('es-GT', {hour:'2-digit', minute:'2-digit'})}`;
  } catch (error) {
    rows.innerHTML = '<tr><td colspan="8" class="empty">No fue posible cargar los registros.</td></tr>';
    tableAlert.textContent = error.message; tableAlert.className = 'alert danger'; tableAlert.hidden = false;
  } finally { refresh.disabled = false; refresh.textContent = 'Actualizar'; }
}

function render(data) {
  count.textContent = `${data.length} ${data.length === 1 ? 'estudiante' : 'estudiantes'}`;
  if (!data.length) { rows.innerHTML = '<tr><td colspan="8" class="empty">No se encontraron estudiantes.</td></tr>'; return; }
  rows.innerHTML = data.map(e => `<tr>
    <td><b class="code">${esc(e.carne)}</b></td><td><div class="student"><span>${iniciales(e)}</span><div><b>${esc(e.nombres)} ${esc(e.apellidos)}</b><small>ID ${e.id}</small></div></div></td>
    <td><a href="mailto:${esc(e.correo)}">${esc(e.correo)}</a></td><td><span class="tag">${esc(e.carrera)}</span></td><td>${esc(e.telefono)}</td>
    <td>${fecha(e.fechaNacimiento)}</td><td>${esc(e.genero)}</td><td>${fechaHora(e.creadoEn)}</td></tr>`).join('');
}
function esc(v){return String(v??'').replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;').replaceAll('"','&quot;');}
function iniciales(e){return esc((e.nombres[0]||'')+(e.apellidos[0]||''));}
function fecha(v){return v ? new Date(`${v}T12:00:00`).toLocaleDateString('es-GT') : '—';}
function fechaHora(v){return v ? new Date(v).toLocaleString('es-GT',{dateStyle:'short',timeStyle:'short'}) : '—';}
search.addEventListener('input',()=>{clearTimeout(timer);timer=setTimeout(cargar,300);}); refresh.addEventListener('click',cargar); cargar();

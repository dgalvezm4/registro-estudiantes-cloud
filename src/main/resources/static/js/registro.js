const form = document.querySelector('#studentForm');
const submitButton = document.querySelector('#submitButton');
const formStatus = document.querySelector('#formStatus');
const alertBox = document.querySelector('#alert');
let captchaToken = '';

window.captchaCompletado = token => { captchaToken = token; document.querySelector('#captchaError').textContent = ''; actualizarEstado(); };
window.captchaExpirado = () => { captchaToken = ''; document.querySelector('#captchaError').textContent = 'La verificación expiró. Complétala nuevamente.'; actualizarEstado(); };
window.captchaError = () => { captchaToken = ''; document.querySelector('#captchaError').textContent = 'No fue posible cargar reCAPTCHA.'; actualizarEstado(); };

function validarCampo(campo) {
  const contenedor = campo.closest('.field');
  if (!contenedor) return campo.checkValidity();
  const error = contenedor.querySelector('.error');
  let mensaje = '';
  if (campo.validity.valueMissing) mensaje = campo.tagName === 'SELECT' ? 'Seleccione una opción.' : 'Este campo es obligatorio.';
  else if (campo.validity.tooShort) mensaje = `Debe contener al menos ${campo.minLength} caracteres.`;
  else if (campo.validity.typeMismatch) mensaje = 'Ingrese un correo electrónico válido.';
  else if (campo.validity.patternMismatch) mensaje = campo.name === 'telefono' ? 'Use un número guatemalteco como 5555-1234.' : 'El formato ingresado no es válido.';
  else if (campo.validity.rangeOverflow) mensaje = 'El estudiante debe tener al menos 16 años.';
  error.textContent = mensaje;
  contenedor.classList.toggle('invalid', Boolean(mensaje));
  return !mensaje;
}

function actualizarEstado() {
  const controles = [...form.querySelectorAll('input:not([type=radio]), select')];
  const genero = form.querySelector('input[name=genero]:checked');
  const camposValidos = controles.every(c => c.checkValidity()) && Boolean(genero);
  submitButton.disabled = !(camposValidos && captchaToken);
  formStatus.textContent = submitButton.disabled ? 'Completa todos los campos y reCAPTCHA' : 'Formulario listo para enviar';
}

form.querySelectorAll('input, select').forEach(campo => {
  campo.addEventListener('input', () => { validarCampo(campo); actualizarEstado(); });
  campo.addEventListener('blur', () => validarCampo(campo));
  campo.addEventListener('change', actualizarEstado);
});

form.addEventListener('submit', async event => {
  event.preventDefault();
  const controles = [...form.querySelectorAll('input:not([type=radio]), select')];
  controles.forEach(validarCampo);
  if (!form.checkValidity() || !captchaToken) { actualizarEstado(); return; }
  submitButton.disabled = true; submitButton.textContent = 'Enviando…'; ocultarAlerta();
  const data = Object.fromEntries(new FormData(form).entries());
  data.captchaToken = captchaToken;
  try {
    const response = await fetch('/api/estudiantes', {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(data)});
    const result = await response.json();
    if (!response.ok) throw new Error(result.mensaje || Object.values(result.errores || {})[0] || 'No fue posible registrar al estudiante');
    mostrarAlerta(result.mensaje, 'success');
    form.reset(); captchaToken = ''; if (window.grecaptcha) grecaptcha.reset();
  } catch (error) { mostrarAlerta(error.message, 'danger'); if (window.grecaptcha) grecaptcha.reset(); captchaToken = ''; }
  finally { submitButton.textContent = 'Registrar estudiante'; actualizarEstado(); window.scrollTo({top:0, behavior:'smooth'}); }
});

function mostrarAlerta(texto, tipo) { alertBox.textContent = texto; alertBox.className = `alert ${tipo}`; alertBox.hidden = false; }
function ocultarAlerta() { alertBox.hidden = true; }
actualizarEstado();

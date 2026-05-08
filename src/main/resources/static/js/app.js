// Cargar compañías al iniciar
window.addEventListener('DOMContentLoaded', () => {
    cargarCompanias();
});

async function cargarCompanias() {
    try {
        const res = await fetch(`${API_BASE}/companias`);
        const companias = await res.json();
        const grid = document.getElementById('companias-grid');
        grid.innerHTML = '';
        companias.forEach(c => {
            const div = document.createElement('div');
            div.className = 'compania-card';
            div.innerHTML = `
                <div class="nombre">${c.nombre}</div>
                <div class="codigo">${c.codigoProductor
                ? 'Cod: ' + c.codigoProductor
                : 'Sin código'}</div>
            `;
            div.onclick = () => mostrarFormulario(c);
            grid.appendChild(div);
        });
    } catch(e) {
        mostrarToast('Error al cargar compañías', 'error');
    }
}

async function guardarPoliza(event) {
    event.preventDefault();
    const companiaId = document.getElementById('form-compania-id').value;
    const codProd = document.getElementById('form-codigo-productor').value;
    const vencimiento = document.getElementById('input-vencimiento').value;

    const poliza = {
        fechaVigencia: document.getElementById('input-fecha').value,
        fechaVencimiento: vencimiento || null,
        productor: 'MELO',
        cliente: document.getElementById('input-cliente').value,
        compania: { id: parseInt(companiaId) },
        ramo: document.getElementById('select-ramo').value,
        codigoOrganizador: 'MENDOZA BROKER SRL',
        codigoProductor: codProd === '—' ? null : codProd,
        numeroPoliza: document.getElementById('input-poliza').value,
        comisionTotal: document.getElementById('input-com-total').value
            ? parseFloat(document.getElementById('input-com-total').value) : null,
        comisionComercial: document.getElementById('input-com-comercial').value
            ? parseFloat(document.getElementById('input-com-comercial').value) : null,
        comisionBroker: document.getElementById('input-com-broker').value
            ? parseFloat(document.getElementById('input-com-broker').value) : null
    };

    try {
        const res = await fetch(`${API_BASE}/polizas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(poliza)
        });
        if (res.ok) {
            mostrarToast('✅ Póliza guardada correctamente');
            setTimeout(() => mostrarVer(null), 1500);
        } else {
            mostrarToast('❌ Error al guardar la póliza', 'error');
        }
    } catch(e) {
        mostrarToast('❌ Error de conexión', 'error');
    }
}

async function cargarPolizas(companiaId) {
    const tbody = document.getElementById('tabla-body');
    tbody.innerHTML = '<tr><td colspan="12" class="tabla-vacia">Cargando...</td></tr>';
    try {
        const url = companiaId
            ? `${API_BASE}/polizas/compania/${companiaId}`
            : `${API_BASE}/polizas`;
        const res = await fetch(url);
        const polizas = await res.json();
        if (polizas.length === 0) {
            tbody.innerHTML =
                '<tr><td colspan="12" class="tabla-vacia">No hay pólizas para mostrar</td></tr>';
            return;
        }
        tbody.innerHTML = polizas.map(p => {
            const fecha = p.fechaVigencia
                ? new Date(p.fechaVigencia + 'T00:00:00')
                    .toLocaleDateString('es-AR')
                : '—';
            const fmt = (v) => v != null
                ? new Intl.NumberFormat('es-AR',
                    { style: 'currency', currency: 'ARS' }).format(v)
                : '—';
            return `
                <tr>
                    <td>${fecha}</td>
                    <td>${p.productor || 'MELO'}</td>
                    <td><strong>${p.cliente || '—'}</strong></td>
                    <td><span class="chip-compania">
                        ${p.compania ? p.compania.nombre : '—'}
                    </span></td>
                    <td>${p.ramo || '—'}</td>
                    <td>${p.codigoOrganizador || 'MENDOZA BROKER SRL'}</td>
                    <td>${p.codigoProductor || '—'}</td>
                    <td>${p.numeroPoliza || '—'}</td>
                    <td>${fmt(p.comisionTotal)}</td>
                    <td>${fmt(p.comisionComercial)}</td>
                    <td>${fmt(p.comisionBroker)}</td>
                    <td>
                        <button class="btn btn-secondary"
                            style="padding:5px 10px; font-size:11px;"
                            onclick="eliminarPoliza(${p.id})">
                            🗑
                        </button>
                    </td>
                </tr>
            `;
        }).join('');
    } catch(e) {
        tbody.innerHTML =
            '<tr><td colspan="12" class="tabla-vacia">❌ Error al cargar</td></tr>';
    }
}

async function eliminarPoliza(id) {
    if (!confirm('¿Seguro que querés eliminar esta póliza?')) return;
    try {
        const res = await fetch(`${API_BASE}/polizas/${id}`,
            { method: 'DELETE' });
        if (res.ok) {
            mostrarToast('✅ Póliza eliminada');
            cargarPolizas(verCompaniaActualId);
        } else {
            mostrarToast('❌ Error al eliminar', 'error');
        }
    } catch(e) {
        mostrarToast('❌ Error de conexión', 'error');
    }
}
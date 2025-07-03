package com.FinZen.services;

import com.FinZen.models.DTOS.PrompResponseDto;
import com.FinZen.models.DTOS.PromptRequestDto;
import com.FinZen.models.Entities.Cuenta;
import com.FinZen.models.Entities.Deuda;
import com.FinZen.models.Entities.Gastos;
import com.FinZen.models.Entities.Ingresos;
import com.FinZen.models.Entities.Meta;
import com.FinZen.models.Entities.Presupuesto;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.repository.CuentaRepository;
import com.FinZen.repository.DeudaRepository;
import com.FinZen.repository.GastosRepository;
import com.FinZen.repository.IngresosRepository;
import com.FinZen.repository.MetaRepository;
import com.FinZen.repository.PresupuestoRepository;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class GptServices {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private GastosRepository gastosRepository;

    @Autowired
    private IngresosRepository ingresoRepository;

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired  
    private CuentaRepository cuentaRepository;

    @Autowired
    private DeudaRepository deudaRepository;

    @Autowired
    private UsuariosServices usuariosServices;


    public PrompResponseDto sendRequestToOpenAi(PromptRequestDto promptRequestDto, Long idUsuario) {
        if (promptRequestDto.prompt() == null || promptRequestDto.prompt().trim().isEmpty()) {
            return new PrompResponseDto("El mensaje no puede estar vacío. Por favor, escribe una pregunta relacionada con tus finanzas.");
        }

        String contextoGastos = obtenerContextoGastosPorUsuario(idUsuario);
        String contextoIngresos = obtenerContextoIngresosPorUsuario(idUsuario);
        String contextoMetas = obtenerContextoMetasPorUsuario(idUsuario);
        String contextoPresupuestos = ObtenerPresupuestosporUsuario(idUsuario);
        String contextoCuentas = ObtenerContextoPorcuenta(idUsuario);
        String contextoDeudas = ObtenerDeudaPorIdUsuario(idUsuario);
        String contextoUsuer =conextoUser(idUsuario);


        String response = chatClient.prompt()
                .system("""
Eres Zennin, una ardilla que esta como asistente virtual financiero especializado del aplicativo FinZen.

Tu objetivo es ayudar a los usuarios a comprender su situación financiera. Puedes responder preguntas relacionadas con:

- Inversiones (como CDT, acciones, fondos), con una cantidad de dinero o sin ella, se breve y no des un mesaje tan largo .
- Ahorros, se breve y no des un mesaje tan largo.
- Tipos de cuentas, se breve y no des un mesaje tan largo.
- Recomendaciones financieras, se breve y no des un mesaje tan largo.
- Todo lo que tenga que ver con finanzas, se breve y no des un mesaje tan largo.
- cada vez que te dirijas al usuario primero hazlo  por el nombre de usuario, se breve y no des un mesaje tan largo

Tu moneda de referencia es el peso colombiano (COP). Si mencionas cantidades, hazlo en COP.

Si la pregunta no está relacionada con finanzas o el sistema FinZen, responde con:
"Lo siento, no puedo ayudarte con esa consulta."
""")
                .user("Datos  personales y financieros del usuario:\n"+contextoUsuer + contextoGastos + "\n" + contextoIngresos +
                        contextoMetas+contextoPresupuestos+contextoCuentas+contextoDeudas+"\n\nPregunta del usuario: " + promptRequestDto.prompt())
                .call()
                .content();

        return new PrompResponseDto(response);
    }



    public PrompResponseDto sendRequestToOpenAi2(PromptRequestDto promptRequestDto) {
        if (promptRequestDto.prompt() == null || promptRequestDto.prompt().trim().isEmpty()) {
            return new PrompResponseDto("El mensaje no puede estar vacío. Por favor, escribe una pregunta relacionada con tus finanzas.");
        }

        String response = chatClient.prompt()
                .system("""
Eres Zennin, un asistente virtual financiero especializado del aplicativo FinZen.

Tu objetivo es ayudar a los usuarios a comprender su situación financiera. Puedes responder preguntas relacionadas con:

- Inversiones (como CDT, acciones, fondos), con una cantidad de dinero o sin ella.
- Ahorros.
- Tipos de cuentas.
- Recomendaciones financieras.
- Todo lo que tenga que ver con finanzas.

Tu moneda de referencia es el peso colombiano (COP). Si mencionas cantidades, hazlo en COP.

Si la pregunta no está relacionada con finanzas o el sistema FinZen, responde con:
"Lo siento, no puedo ayudarte con esa consulta."
""")
                .user(promptRequestDto.prompt())
                .call()
                .content();

        return new PrompResponseDto(response);
    }


    public PrompResponseDto notificaTions(Long idUsuario) {
        String contextoGastos = obtenerContextoGastosPorUsuario(idUsuario);
        String contextoIngresos = obtenerContextoIngresosPorUsuario(idUsuario);
        String contextoMetas = obtenerContextoMetasPorUsuario(idUsuario);
        String contextoPresupuestos = ObtenerPresupuestosporUsuario(idUsuario);
        String contextoCuentas = ObtenerContextoPorcuenta(idUsuario);
        String contextoDeudas = ObtenerDeudaPorIdUsuario(idUsuario);
        String contextoUsuer = conextoUser(idUsuario);
    
        String response = chatClient.prompt()
                .system("""
                        Dame un consejo segun mis datos siempre llamame por mi nombre 
                        """)
                .user("Datos  personales y financieros del usuario:\n"+contextoUsuer + contextoGastos + "\n" + contextoIngresos +
                        contextoMetas+contextoPresupuestos+contextoCuentas+contextoDeudas+"\n\nPregunta del usuario: " )
                .call()
                .content();
        
        
        return new PrompResponseDto(response);
    }





    public String obtenerContextoIngresosPorUsuario(Long idUsuario) {
        List<Ingresos> ingresos = ingresoRepository.findByUsuarioId(idUsuario);

        if (ingresos.isEmpty()) {
            return "El usuario no tiene ingresos registrados.";
        }

        StringBuilder contexto = new StringBuilder("Ingresos del usuario:\n");
        for (Ingresos ingreso : ingresos) {
            contexto.append("- ")
                    .append(ingreso.getNombre()).append(": $")
                    .append(ingreso.getMonto()).append(" (")
                    .append(ingreso.getDescripcion()).append(")\n");
        }
        return contexto.toString();
    }

    public String obtenerContextoMetasPorUsuario(Long idUsuario) {
        List<Meta> metas = metaRepository.findByUsuarioId(idUsuario);

        if (metas.isEmpty()) {
            return "El usuario no tiene metas registradas.";
        }

        StringBuilder contexto = new StringBuilder("metas del usuario:\n");
        for (Meta meta : metas) {
            contexto.append("- ")
                    .append(meta.getTitulo()) 
                    .append(meta.getDescripcion())
                    .append(meta.getFechaInicio())
                    .append(meta.getFechaLimite()).append(")\n");
        }
        return contexto.toString();
    }

    public String obtenerContextoGastosPorUsuario(Long idUsuario) {

    
        List<Gastos> gastos = gastosRepository.getGastosByUsuarioId(idUsuario);

        if (gastos.isEmpty()) {
            return "El usuario no tiene gastos registrados.";
        }

        StringBuilder contexto = new StringBuilder("Gastos del usuario:\n");
        for (Gastos gasto : gastos) {
            contexto.append("- ")
                    .append(gasto.getNombre()).append(": $")
                    .append(gasto.getMonto())
                    .append(" (Fecha: ").append(gasto.getFecha()).append(")\n");
        }
        return contexto.toString();
    }

    public String ObtenerPresupuestosporUsuario(Long idUsuario) {

        List<Presupuesto> presupuestos = presupuestoRepository.findByUsuarioId(idUsuario);

        if (presupuestos.isEmpty()) {
            return "El usuario no tiene presupuestos registrados.";
        }

        StringBuilder contexto = new StringBuilder("Presupuestos del usuario:\n");
        for (Presupuesto presupuesto : presupuestos) {
            contexto.append("- ")
                    .append(presupuesto.getNombre()).append(": $")
                    .append(presupuesto.getMontoAsignado())
                    .append("\n");
        }
        return contexto.toString();
    }

    public String ObtenerContextoPorcuenta(Long idUsuario){
        List<Cuenta> cuentas = cuentaRepository.findByUsuariosIdUsuario(idUsuario);

        if (cuentas.isEmpty()) {
            return "El usuario no tiene cuentas registradas.";
        }
        StringBuilder contexto = new StringBuilder("Cuentas del usuario:\n");
        for (Cuenta cuenta : cuentas) {
            contexto.append("- ")
                    .append(cuenta.getNombre()).append(": $")
                    .append(cuenta.getMonto()).append(" (")
                    .append(cuenta.getMontoLibre()).append(")\n");
        }
        return contexto.toString();
    }

    public String ObtenerDeudaPorIdUsuario(Long idUsuario){
        List<Deuda> deudas = deudaRepository.findByIdUsuario(idUsuario);

        if (deudas.isEmpty()) {
            return "El usuario no tiene deudas registradas.";
        }
        StringBuilder contexto = new StringBuilder("deudas del usuario:\n");
        for (Deuda deuda : deudas) {
            contexto.append("- ")
                    .append(deuda.getNombre()).append(": $")
                    .append(deuda.getEstado()).append(" (")
                    .append(deuda.getMonto())
                    .append(deuda.getMontoPagado())
                    .append(deuda.getFechaCreacion())
                    .append(deuda.getFechaVencimiento()).append(")\n");
        }
        return contexto.toString();
    }

    public String conextoUser(Long id) {
    
        Usuarios usuario = usuariosServices.finById(id);
        if (usuario == null) {
            return "no se encontro el usuario ";
        }
        StringBuilder contexto = new StringBuilder("datos del usuario:\n");
        contexto.append(usuario.getNombre())
                .append(usuario.getCorreo())
                .append(usuario.getIngresoMensual())
                .append(usuario.getNombreUsuario())
                .append(usuario.getTipoPersona())
                .append(usuario.getPaisResidencia());
                
        return contexto.toString();
    }



}

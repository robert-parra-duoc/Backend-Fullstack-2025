package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.LoginRequest;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //@PostMapping
    //public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario nuevoUsuario) {
    //    Usuario guardado = usuarioRepository.save(nuevoUsuario);
    //    return ResponseEntity.ok(guardado);
    //}

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario nuevoUsuario) {
    // Forzar que cualquier usuario registrado sea "usuario"
    nuevoUsuario.setRol("usuario");

    Usuario guardado = usuarioRepository.save(nuevoUsuario);
    return ResponseEntity.ok(guardado);
    }


    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest loginRequest) {

        return usuarioRepository
                .findByCorreoAndContrasena(loginRequest.getCorreo(), loginRequest.getContrasena())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id,
                                                     @RequestBody Usuario datosActualizados) {

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setNombre(datosActualizados.getNombre());
        usuario.setCorreo(datosActualizados.getCorreo());
        usuario.setContrasena(datosActualizados.getContrasena());

        Usuario actualizado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.co.controller;

import com.co.model.Asignacion;
import com.co.model.Bus;
import com.co.model.Horario;
import com.co.model.Ruta;
import com.co.service.AsignacionService;
import com.co.service.BusService;
import com.co.service.HorarioService;
import com.co.service.RutaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sistema")
public class AsignacionController {

    @Autowired
    private AsignacionService asignacionService;

    @Autowired
    private BusService busService;

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private RutaService rutaService;

    @GetMapping("/list")
    public ModelAndView listarSistemas() {
        List<Asignacion> asignaciones = asignacionService.obtenerTodos();
        ModelAndView modelAndView = new ModelAndView("sistema-list");
        modelAndView.addObject("sistemas", asignaciones);
        return modelAndView;
    }

    @GetMapping("/edit-form/{id}")
    public ModelAndView formularioEditarSistema(@PathVariable Long id) {
        Optional<Asignacion> asignacionOpt = asignacionService.obtenerPorId(id);
        if (!asignacionOpt.isPresent()) {
            return new ModelAndView("redirect:/sistema/list");
        }

        Asignacion asignacion = asignacionOpt.get();
        List<Bus> buses = busService.findAll();
        List<Horario> horarios = horarioService.findAll();
        List<Ruta> rutas = rutaService.obtenerTodos();

        ModelAndView modelAndView = new ModelAndView("sistema-form");
        modelAndView.addObject("sistema", asignacion);
        modelAndView.addObject("buses", buses);
        modelAndView.addObject("horarios", horarios);
        modelAndView.addObject("rutas", rutas);
        return modelAndView;
    }

    @GetMapping("/create-form")
    public ModelAndView formularioCrearSistema() {
        Asignacion nuevaAsignacion = new Asignacion();
        List<Bus> buses = busService.findAll();
        List<Horario> horarios = horarioService.findAll();
        List<Ruta> rutas = rutaService.obtenerTodos();

        ModelAndView modelAndView = new ModelAndView("sistema-form");
        modelAndView.addObject("sistema", nuevaAsignacion);
        modelAndView.addObject("buses", buses);
        modelAndView.addObject("horarios", horarios);
        modelAndView.addObject("rutas", rutas);
        return modelAndView;
    }

    @PostMapping("/save")
    public ModelAndView guardarSistema(@Valid @ModelAttribute("sistema") Asignacion asignacion, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // En caso de error, volver a cargar los datos necesarios para el formulario
            List<Bus> buses = busService.findAll();
            List<Horario> horarios = horarioService.findAll();
            List<Ruta> rutas = rutaService.obtenerTodos();

            model.addAttribute("buses", buses);
            model.addAttribute("horarios", horarios);
            model.addAttribute("rutas", rutas);
            return new ModelAndView("sistema-form", "sistema", asignacion);
        }

        asignacionService.guardar(asignacion);
        return new ModelAndView(new RedirectView("/sistema/list"));
    }

    @GetMapping("/delete/{id}")
    public RedirectView eliminarSistema(@PathVariable Long id) {
        asignacionService.eliminar(id);
        return new RedirectView("/sistema/list");
    }
}

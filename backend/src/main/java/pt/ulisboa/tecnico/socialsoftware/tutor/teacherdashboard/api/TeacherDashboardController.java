package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.TeacherDashboardDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.services.TeacherDashboardService;

import java.security.Principal;

@RestController
public class TeacherDashboardController {
    @Autowired
    private TeacherDashboardService teacherDashboardService;

    TeacherDashboardController(TeacherDashboardService teacherDashboardService) {
        this.teacherDashboardService = teacherDashboardService;
    }

    @GetMapping("/teachers/dashboards/executions/{courseExecutionId}")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#courseExecutionId, 'EXECUTION.ACCESS')")
    public TeacherDashboardDto getTeacherDashboard(Principal principal, @PathVariable int courseExecutionId) {
        int teacherId = ((AuthUser) ((Authentication) principal).getPrincipal()).getUser().getId();

        return teacherDashboardService.getTeacherDashboard(courseExecutionId, teacherId);
    }

    @GetMapping("/teachers/dashboards/{teacherDashboardId}/update")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#teacherDashboardId, 'TEACHERDASHBOARD.ACCESS')")
    public void updateTeacherDashboard(Principal principal, @PathVariable int teacherDashboardId) {
        teacherDashboardService.updateTeacherDashboard(teacherDashboardId);
    }

    @DeleteMapping("/teachers/dashboards/{teacherDashboardId}/remove")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#teacherDashboardId, 'TEACHERDASHBOARD.ACCESS')")
    public void removeTeacherDashboard(Principal principal, @PathVariable int teacherDashboardId) {
        teacherDashboardService.removeTeacherDashboard(teacherDashboardId);
    }

    @GetMapping("/teachers/dashboards/{dashboardId}/updateAllTeacherDashboards")
    @PreAuthorize("hasRole('ROLE_TEACHER') and hasPermission(#courseExecutionId, 'EXECUTION.ACCESS')")
    public void updateAllTeacherDashboards() {
        teacherDashboardService.updateAllTeacherDashboards();
    }

}

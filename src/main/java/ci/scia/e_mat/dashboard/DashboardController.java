// package ci.scia.e_mat.dashboard;

// import ci.scia.e_mat.dashboard.DashboardDTO;
// import ci.scia.e_mat.dashboard.DashboardService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/api/dashboard")
// @RequiredArgsConstructor
// public class DashboardController {
//     private final DashboardService dashboardService;

//     @GetMapping
//     public ResponseEntity<DashboardDTO> getDashboardData() {
//         return ResponseEntity.ok(dashboardService.getDashboardData());
//     }
// }

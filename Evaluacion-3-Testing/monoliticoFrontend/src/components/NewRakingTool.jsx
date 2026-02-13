import React, { useState, useRef } from "react";
import { Box, Typography, Paper, CircularProgress, Button } from "@mui/material";
import toolsService from "../services/tools.service";
import toolsReportService from "../services/toolsReport.service";
import reportsService from "../services/reports.service";
import toolsRankingService from "../services/toolsRanking.service";
import { useNavigate, useSearchParams } from "react-router-dom";

const NewRakingTool = () => {
	const [searchParams] = useSearchParams();
	const [loading, setLoading] = useState(false);
	const navigate = useNavigate();
	const tokenRef = useRef(localStorage.getItem("authToken"));
	
	const token = tokenRef.current;
	if (!token) {
		console.error("No auth token found in memory.");
		navigate("/login");
		return null;
	}

	const tokenPayload = JSON.parse(atob(token.split(".")[1]));
	const keycloakClientId = Number(tokenPayload?.id_real);

	const handleGenerateRanking = async () => {
		setLoading(true);
		// Obtener clientId de la URL si existe, si no usar el de Keycloak
		const urlClientId = searchParams.get("clientId");
		const clientId = urlClientId ? parseInt(urlClientId) : keycloakClientId;

		// then we create a list to store the ids of the tools created in toolsReport
		const toolReportIds = [];
		for (const tool of toolsList) {
			const toolReportRes = await toolsReportService.create({
				toolName: tool.tool_name,
				category: tool.category,
				loanCount: tool.loan_count,
			});
			// then we push the values into the list
			toolReportIds.push({
				toolId: toolReportRes.data?.toolIdReport,
				originalToolId: tool.toolId,
				category: tool.category,
				loanCount: tool.loan_count,
				toolName: tool.tool_name,
			});
		}

		// then we create a new report, with the toolsIdRanking set to true
		console.log(clientId);
		const reportRes = await reportsService.create({
			toolsIdRanking: true,
			clientIdReport: clientId,
		});
		const reportId = reportRes.data?.reportId;

		// then we create the ranking entries in toolsRanking
		for (const tool of toolReportIds) {
			await toolsRankingService.create({
				reportId: reportId,
				toolId: tool.toolId,
			});
		}

		setLoading(false);

		window.alert("Ranking de herramientas generado exitosamente");
		if (urlClientId) {
			navigate("/client/list");
		} else {
			navigate("/myreports");
		}
	};

	return (
		<Box sx={{ position: "relative", minHeight: "100vh" }}>
			<Box
				sx={{
					position: "fixed",
					top: 0,
					left: 0,
					width: "100%",
					height: "100%",
					backgroundImage: `url("/fondo.jpg")`,
					backgroundSize: "cover",
					backgroundPosition: "center",
					backgroundRepeat: "no-repeat",
					filter: "blur(8px)",
					zIndex: 0,
				}}
			/>
			<Box sx={{ position: "relative", zIndex: 1, minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center" }}>
				<Paper sx={{ p: 3, maxWidth: 400, width: "100%", background: "rgba(255,255,255,0.95)", textAlign: "center" }}>
					<Typography variant="h5" sx={{ mb: 2 }}>
						¿Desea generar el ranking de herramientas?
					</Typography>
					<Button variant="contained" color="primary" onClick={handleGenerateRanking} disabled={loading}>
						Generar Ranking
					</Button>
					{loading && (
						<Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", mt: 3 }}>
							<CircularProgress />
						</Box>
					)}
				</Paper>
			</Box>
		</Box>
	);
};

export default NewRakingTool;

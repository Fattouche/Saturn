(function () {
	'use strict';

	angular
		.module('saturnApp')
		.controller('SaturnVaultImportController', SaturnVaultImportController);

    SaturnVaultImportController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModal', '$uibModalInstance', 'SaturnVault'];

	function SaturnVaultImportController($timeout, $scope, $stateParams, $uibModal, $uibModalInstance, SaturnVault) {
		var vm = this;

		vm.save = save;
		vm.clear = clear;
		vm.isSaving = false;

		$timeout(function () {
			angular.element('.form-group:eq(1)>input').focus();
		});

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		function save() {
			vm.isSaving = true;
			SaturnVault.import({file: $scope.uploadFile}, onSaveSuccess, onSaveError);
		}

		function onSaveSuccess(result) {
			//$scope.$emit('saturnApp:SaturnVaultUpdate', result);
			$uibModalInstance.close(result);
			console.log(result);
			vm.isSaving = false;
		}

		function onSaveError() {
			vm.isSaving = false;
		}
	}
})();

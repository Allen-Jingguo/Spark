// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

const getList = async function (
  params: {
    name: string;
    stauts?: string;
    type?: string;
  },
  options?: { [key: string]: any },
) {
  return request<API.FLow>('/api/flow/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

export async function stop(
  params: {
    name?: string;
    id?: string;
  },
  options?: { [key: string]: any },
) {
  return request<API.FLow>('/api/flow/stop', {
    method: 'post',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function start(
  params: {
    name?: string;
    id?: string;
  },
  options?: { [key: string]: any },
) {
  return request<API.FLow>('/api/flow/start', {
    method: 'post',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function pause(
  params: {
    name?: string;
    id?: string;
  },
  options?: { [key: string]: any },
) {
  return request<API.FLow>('/api/flow/pause', {
    method: 'post',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function createNew(
  params: {
    name?: string;
    id?: string;
  },
  options?: { [key: string]: any },
) {
  return request<API.FLow>('/api/flow/create_new', {
    method: 'post',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

const getAllInbound = async function () {
  return request<API.KeyValue>('/api/flow/getAllInbound', {
    method: 'GET',
  });
};

const getAllFlowType = async function () {
  return request<API.KeyValue>('/api/flow/getAllFlowType', {
    method: 'GET',
  });
};

const getAllParserType = async function () {
  return request<API.KeyValue>('/api/flow/getAllParserType', {
    method: 'GET',
  });
};

const getAllKeyMapper = async function () {
  return request<API.KeyValue>('/api/flow/getAllKeyMapper', {
    method: 'GET',
  });
};

const getAllFormatter = async function () {
  return request<API.KeyValue>('/api/flow/getAllFormatter', {
    method: 'GET',
  });
};

const getOutboundName = async function () {
  return request<API.KeyValue>('/api/flow/getOutboundName', {
    method: 'GET',
  });
};

export default {
  getList,
  start,
  pause,
  stop,
  createNew,
  getAllInbound,
  getAllFlowType,
  getAllParserType,
  getAllKeyMapper,
  getAllFormatter,
  getOutboundName,
};
